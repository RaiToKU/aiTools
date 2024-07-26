package com.szjlc.smt.ai.sdk.executor.model.baidu;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.baidu.config.BaiduConfig;
import com.szjlc.smt.ai.sdk.executor.model.baidu.utils.AccessTokenUtils;
import com.szjlc.smt.ai.sdk.executor.model.baidu.valobj.*;
import com.szjlc.smt.ai.sdk.executor.model.baidu.valobj.Message;
import com.szjlc.smt.ai.sdk.executor.model.baidu.valobj.Usage;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 百度文心一言大模型
 */
@Slf4j
public class BaiduModelExecutor implements Executor, ParameterHandler<BaiduCompletionRequest>, ResultHandler {

    private final BaiduConfig baiduConfig;

    private final EventSource.Factory factory;

    private final OkHttpClient okHttpClient;

    public BaiduModelExecutor(Configuration configuration) {
        this.baiduConfig = configuration.getBaiduConfig();
        this.okHttpClient = configuration.getOkHttpClient();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        return completions(null, null, completionRequest, eventSourceListener);
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // 1. 核心参数校验；不对用户的传参做更改，只返回错误信息。
        if (!completionRequest.isStream()) {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 2. 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? baiduConfig.getApiHost() : apiHostByUser;
        String apiKey = baiduConfig.getApiKey();
        String apiSecret = baiduConfig.getApiSecret();
        String authHost = baiduConfig.getAuthHost();
        if (apiKeyByUser != null) {
            // wcQzXMCgP8Npb3Vo4SIWF.QZq37XqjanVlOKThk6RZsVcDMR3
            String[] apiKeySecret = apiKeyByUser.split("\\.");
            apiKey = apiKeySecret[0];
            apiSecret = apiKeySecret[1];
        }

        // 3. 获取 token 信息
        String accessToken = AccessTokenUtils.getAccessToken(okHttpClient, apiKey, apiSecret, authHost);

        // 4. 转换参数信息
        BaiduCompletionRequest baiduCompletionRequest = getParameterObject(completionRequest);

        // 5. 构建请求信息
        Request request = new Request.Builder()
                .addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(apiHost.concat(BaiduConfig.CompletionsUrl.valueOf(completionRequest.getModel()).getUrl()).concat("?access_token=").concat(accessToken))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(baiduCompletionRequest)))
                .build();

        return factory.newEventSource(request, eventSourceListener(eventSourceListener));
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest) throws Exception{
        return genImages(null, null, imageRequest);
    }

    @Override
    public ImageResponse genImages(String apiHostByUser, String apiKeyByUser, ImageRequest imageRequest) throws IOException {
        //1.统一转换参数
        BaiduImageRequest baiduImageRequest = BaiduImageRequest.builder()
                .n(imageRequest.getN())
                .size(imageRequest.getSize())
                .prompt(imageRequest.getPrompt())
                .build();

        //2.动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? baiduConfig.getApiHost() : apiHostByUser;
        String apiKey = baiduConfig.getApiKey();
        String apiSecret = baiduConfig.getApiSecret();
        String authHost = baiduConfig.getAuthHost();
        if (apiKeyByUser != null) {
            String[] apiKeySecret = apiKeyByUser.split("\\.");
            apiKey = apiKeySecret[0];
            apiSecret = apiKeySecret[1];
        }

        // 3. 获取 token 信息
        String accessToken = AccessTokenUtils.getAccessToken(okHttpClient, apiKey, apiSecret, authHost);

        //4. 构建请求信息
        String url = BaiduConfig.CompletionsUrl.valueOf(imageRequest.getModel()).getUrl();

        Request request = new Request.Builder()
                .addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(apiHost.concat(url).concat("?access_token=").concat(accessToken))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), JSON.toJSONString(baiduImageRequest)))
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        if (response.isSuccessful() && response.body() != null) {
            return JSON.parseObject(response.body().string(), ImageResponse.class);
        }else {
            throw new IOException("Failed to get image response");
        }
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
    public BaiduCompletionRequest getParameterObject(CompletionRequest completionRequest) {

        // 信息转换，需要带上历史信息
        List<Message> wenXinMessages = new ArrayList<>();
        List<com.szjlc.smt.ai.sdk.executor.parameter.Message> messages = completionRequest.getMessages();
        for (com.szjlc.smt.ai.sdk.executor.parameter.Message message : messages) {
            Message messageVo = new Message();
            messageVo.setRole(message.getRole());
            messageVo.setContent(message.getContent());
            wenXinMessages.add(messageVo);
        }

        // 封装参数
        BaiduCompletionRequest baiduCompletionRequest = new BaiduCompletionRequest();
        baiduCompletionRequest.setStream(completionRequest.isStream());
        baiduCompletionRequest.setTopP(completionRequest.getTopP());
        baiduCompletionRequest.setTemperature(completionRequest.getTemperature());
        baiduCompletionRequest.setMessages(wenXinMessages);

        return baiduCompletionRequest;
    }


    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                BaiduCompletionResponse response = JSON.parseObject(data, BaiduCompletionResponse.class);
                CompletionResponse completionResponse = new CompletionResponse();
                List<ChatChoice> choices = new ArrayList<>();
                ChatChoice chatChoice = new ChatChoice();
                chatChoice.setDelta(com.szjlc.smt.ai.sdk.executor.parameter.Message.builder()
                        .name("")
                        .role(CompletionRequest.Role.SYSTEM)
                        .content(response.getResult())
                        .build());
                choices.add(chatChoice);
                completionResponse.setChoices(choices);
                // 未结束对话
                if (!response.getIsEnd()) {
                    eventSourceListener.onEvent(eventSource, id, type, JSON.toJSONString(completionResponse));
                } else {
                    // 封装额度
                    Usage usage = response.getUsage();
                    com.szjlc.smt.ai.sdk.executor.parameter.Usage openaiUsage = new com.szjlc.smt.ai.sdk.executor.parameter.Usage();
                    openaiUsage.setPromptTokens(usage.getPromptTokens());
                    openaiUsage.setCompletionTokens(usage.getCompletionTokens());
                    openaiUsage.setTotalTokens(usage.getTotalTokens());
                    // 封装结束
                    chatChoice.setFinishReason("stop");
                    chatChoice.setDelta(com.szjlc.smt.ai.sdk.executor.parameter.Message.builder()
                            .name("")
                            .role(CompletionRequest.Role.SYSTEM)
                            .content(response.getResult())
                            .build());
                    choices.add(chatChoice);
                    // 构建结果
                    completionResponse.setUsage(openaiUsage);
                    completionResponse.setCreated(System.currentTimeMillis());
                    // 返回数据
                    eventSourceListener.onEvent(eventSource, null, null, JSON.toJSONString(completionResponse));
                }
            }

            @Override
            public void onOpen(EventSource eventSource, Response response) {
                eventSourceListener.onOpen(eventSource, response);
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
