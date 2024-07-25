package com.szjlc.smt.ai.sdk.executor.model.aliyun;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.config.AliModelConfig;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.valobj.AliModelCompletionRequest;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.valobj.AliModelCompletionResponse;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.valobj.FinishReason;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 通义千问 执行器
 *
 * @author Vanffer
 */
@Slf4j
public class AliModelExecutor implements Executor, ResultHandler, ParameterHandler<AliModelCompletionRequest> {
    private final EventSource.Factory factory;
    private final AliModelConfig aliModelConfig;

    public AliModelExecutor(Configuration configuration) {
        this.aliModelConfig = configuration.getAliModelConfig();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // 1. 转换参数信息
        AliModelCompletionRequest aliModelCompletionRequest = getParameterObject(completionRequest);
        // 2. 构建请求信息
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + aliModelConfig.getApiKey())
                .header("Accept", "text/event-stream")
                .url(aliModelConfig.getApiHost().concat(aliModelConfig.getV1_completions()))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), JSON.toJSONString(aliModelCompletionRequest)))
                .build();
        // 3. 返回事件结果
        return factory.newEventSource(request, eventSourceListener(eventSourceListener));
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
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
    public AliModelCompletionRequest getParameterObject(CompletionRequest completionRequest) {
        AliModelCompletionRequest aliModelCompletionRequest = new AliModelCompletionRequest();
        aliModelCompletionRequest.setModel(completionRequest.getModel());
        AliModelCompletionRequest.Input input = new AliModelCompletionRequest.Input();
        List<Message> messages = completionRequest.getMessages();
        List<com.szjlc.smt.ai.sdk.executor.model.aliyun.valobj.Message> aliMessages = new ArrayList<>();
        for (Message message : messages) {
            aliMessages.add(com.szjlc.smt.ai.sdk.executor.model.aliyun.valobj.Message.builder()
                    .role(message.getRole())
                    .content(message.getContent())
                    .build());
        }
        input.setMessages(aliMessages);
        aliModelCompletionRequest.setInput(input);
        aliModelCompletionRequest.setParameters(AliModelCompletionRequest.Parameters.builder()
                .incrementalOutput(true)
                .build());
        System.out.println(JSON.toJSONString(aliModelCompletionRequest));
        return aliModelCompletionRequest;
    }


    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                AliModelCompletionResponse response = JSON.parseObject(data, AliModelCompletionResponse.class);
                if (FinishReason.CONTINUE.getCode().equals(response.getOutput().getFinish_reason())) {
                    CompletionResponse completionResponse = new CompletionResponse();
                    List<ChatChoice> choices = new ArrayList<>();
                    ChatChoice chatChoice = new ChatChoice();
                    chatChoice.setDelta(Message.builder()
                            .role(CompletionRequest.Role.SYSTEM)
                            .name("")
                            .content(response.getOutput().getText())
                            .build());
                    choices.add(chatChoice);
                    completionResponse.setChoices(choices);
                    eventSourceListener.onEvent(eventSource, id, type, JSON.toJSONString(completionResponse));
                } else if (FinishReason.STOP.getCode().equals(response.getOutput().getFinish_reason())) {
                    AliModelCompletionResponse.Usage aliUsage = response.getUsage();
                    Usage usage = new Usage();
                    usage.setPromptTokens(aliUsage.getInput_tokens());
                    usage.setCompletionTokens(aliUsage.getOutput_tokens());
                    usage.setTotalTokens(aliUsage.getTotal_tokens());
                    List<ChatChoice> choices = new ArrayList<>();
                    ChatChoice chatChoice = new ChatChoice();
                    chatChoice.setFinishReason("stop");
                    chatChoice.setDelta(Message.builder()
                            .name("")
                            .role(CompletionRequest.Role.SYSTEM)
                            .content(response.getOutput().getText())
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
                    onClosed(eventSource);
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                eventSourceListener.onClosed(eventSource);
            }

            @Override
            public void onOpen(EventSource eventSource, Response response) {
                eventSourceListener.onOpen(eventSource, response);
            }

            @Override
            public void onFailure(EventSource eventSource, @javax.annotation.Nullable Throwable t, @javax.annotation.Nullable Response response) {
                eventSourceListener.onFailure(eventSource, t, response);
            }

        };
    }
}
