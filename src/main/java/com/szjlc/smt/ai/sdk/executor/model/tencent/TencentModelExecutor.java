package com.szjlc.smt.ai.sdk.executor.model.tencent;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.tencent.config.TencentConfig;
import com.szjlc.smt.ai.sdk.executor.model.tencent.utils.SecurityUtils;
import com.szjlc.smt.ai.sdk.executor.model.tencent.valobj.Action;
import com.szjlc.smt.ai.sdk.executor.model.tencent.valobj.Message;
import com.szjlc.smt.ai.sdk.executor.model.tencent.valobj.TencentCompletionRequest;
import com.szjlc.smt.ai.sdk.executor.model.tencent.valobj.TencentCompletionResponse;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 腾讯混元执行器
 *
 * @author ion1ze
 */
public class TencentModelExecutor implements Executor, ParameterHandler<TencentCompletionRequest>, ResultHandler {

    private static final String FINISH_REASON_STOP = "stop";

    private final EventSource.Factory factory;
    private final TencentConfig tencentConfig;

    public TencentModelExecutor(Configuration configuration) {
        this.tencentConfig = configuration.getTencentConfig();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // 1. 转换参数信息
        TencentCompletionRequest tencentCompletionRequest = this.getParameterObject(completionRequest);
        String action = Action.of(completionRequest.getModel());
        String version = this.tencentConfig.getApiVersion();
        String region = this.tencentConfig.getRegion();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        String requestPayload = JSON.toJSONString(tencentCompletionRequest);
        String authorization = SecurityUtils.getAuthorization(requestPayload, timestamp, this.tencentConfig);

        Request request = new Request.Builder()
                .header("Authorization", authorization)
                .header("X-TC-Action", action)
                .header("X-TC-Timestamp", timestamp)
                .header("X-TC-Version", version)
                .header("X-TC-Region", region)
                .header("Accept", Configuration.SSE_CONTENT_TYPE)
                .url(this.tencentConfig.getApiHost())
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), requestPayload))
                .build();
        // 3. 返回事件结果
        return factory.newEventSource(request, eventSourceListener(eventSourceListener));
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        throw new NotImplementedException("没有实现此方法");
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest) {
        return null;
    }

    @Override
    public ImageResponse genImages(String apiHostByUser, String apiKeyByUser, ImageRequest imageRequest) {
        return null;
    }

    @Override
    public ImageResponse editImages(ImageEditRequest imageEditRequest) throws Exception {
        return null;
    }

    @Override
    public ImageResponse editImages(String apiHostByUser, String apiKeyByUser, ImageEditRequest imageEditRequest) throws Exception {
        return null;
    }

    @Override
    public EventSource pictureUnderstanding(PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
    }

    @Override
    public EventSource pictureUnderstanding(String apiHostByUser, String apiKeyByUser, PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
    }

    @Override
    public TencentCompletionRequest getParameterObject(CompletionRequest completionRequest) {
        List<Message> messages = completionRequest.getMessages().stream()
                .map(Message::of)
                .collect(Collectors.toList());

        return TencentCompletionRequest.builder()
                .messages(messages)
                .temperature(completionRequest.getTemperature())
                .topP(completionRequest.getTopP())
                .build();
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return new EventSourceListener() {
            @Override
            public void onOpen(EventSource eventSource, Response response) {
                eventSourceListener.onOpen(eventSource, response);
            }

            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                // 这里 type 为 null,所以从 data 中解析结束状态
                TencentCompletionResponse response = JSON.parseObject(data, TencentCompletionResponse.class);
                TencentCompletionResponse.Choice choice = response.getChoices().get(0);

                final boolean stopped = FINISH_REASON_STOP.equals(choice.getFinishReason());
                if (stopped) {
                    TencentCompletionResponse.Usage tencentUsage = response.getUsage();

                    // 封装额度信息
                    Usage usage = new Usage();
                    usage.setPromptTokens(tencentUsage.getPromptTokens());
                    usage.setCompletionTokens(tencentUsage.getCompletionTokens());
                    usage.setTotalTokens(tencentUsage.getTotalTokens());

                    // 封装结束
                    List<ChatChoice> choices = new ArrayList<>();
                    ChatChoice chatChoice = new ChatChoice();
                    chatChoice.setFinishReason("stop");

                    String content = choice.getDelta().getContent();

                    chatChoice.setDelta(com.szjlc.smt.ai.sdk.executor.parameter.Message.builder()
                            .name("")
                            .role(CompletionRequest.Role.SYSTEM)
                            .content(content)
                            .build());
                    choices.add(chatChoice);

                    // 构建结果
                    CompletionResponse completionResponse = new CompletionResponse();
                    completionResponse.setChoices(choices);
                    completionResponse.setUsage(usage);
                    completionResponse.setCreated(System.currentTimeMillis());
                    // 返回数据
                    eventSourceListener.onEvent(eventSource, id, type, JSON.toJSONString(completionResponse));
                } else {
                    CompletionResponse completionResponse = new CompletionResponse();
                    List<ChatChoice> choices = new ArrayList<>();
                    ChatChoice chatChoice = new ChatChoice();

                    String content = choice.getDelta().getContent();

                    chatChoice.setDelta(com.szjlc.smt.ai.sdk.executor.parameter.Message.builder()
                            .name("")
                            .role(CompletionRequest.Role.SYSTEM)
                            .content(content)
                            .build());
                    choices.add(chatChoice);
                    completionResponse.setChoices(choices);
                    completionResponse.setCreated(System.currentTimeMillis());
                    eventSourceListener.onEvent(eventSource, id, type, JSON.toJSONString(completionResponse));
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                eventSourceListener.onClosed(eventSource);
            }

            @Override
            public void onFailure(EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                eventSourceListener.onFailure(eventSource, t, response);
            }
        };
    }
}
