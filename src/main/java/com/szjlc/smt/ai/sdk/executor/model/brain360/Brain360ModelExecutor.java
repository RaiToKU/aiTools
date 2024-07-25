package com.szjlc.smt.ai.sdk.executor.model.brain360;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.brain360.config.Brain360Config;
import com.szjlc.smt.ai.sdk.executor.model.brain360.valobj.Brain360CompletionRequest;
import com.szjlc.smt.ai.sdk.executor.model.brain360.valobj.Message;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.ArrayList;
import java.util.List;

public class Brain360ModelExecutor implements Executor, ParameterHandler<Brain360CompletionRequest>, ResultHandler {


    /**
     * 配置信息
     */
    private final Brain360Config brain360Config;
    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    public Brain360ModelExecutor(Configuration configuration) {
        this.brain360Config = configuration.getBrain360Config();
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
        String apiHost = null == apiHostByUser ? brain360Config.getApiHost() : apiHostByUser;
        String apiKey = null == apiKeyByUser ? brain360Config.getApiKey() : apiKeyByUser;

        // 3. 转换参数信息
        Brain360CompletionRequest brain360CompletionRequest = getParameterObject(completionRequest);

        // 4. 构建请求信息
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + apiKey)
                .url(apiHost.concat(brain360Config.getV1_chat_completions()))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON),
                        new ObjectMapper().writeValueAsString(brain360CompletionRequest)))
                .build();

        // 5. 返回事件结果
        return factory.newEventSource(request, eventSourceListener);
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
    public Brain360CompletionRequest getParameterObject(CompletionRequest completionRequest) {
        List<Message> brainMessage = new ArrayList<Message>();
        for (com.szjlc.smt.ai.sdk.executor.parameter.Message message : completionRequest.getMessages()) {
            Message messageVo = new Message();
            messageVo.setContent(messageVo.getContent());
            messageVo.setRole(message.getRole());
            brainMessage.add(messageVo);
        }

        Brain360CompletionRequest brain360CompletionRequest = new Brain360CompletionRequest();
        brain360CompletionRequest.setModel(completionRequest.getModel());
        brain360CompletionRequest.setTemperature(completionRequest.getTemperature());
        brain360CompletionRequest.setTopP(completionRequest.getTopP());
        brain360CompletionRequest.setStream(completionRequest.isStream());
        brain360CompletionRequest.setMessages(brainMessage);
        return brain360CompletionRequest;
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return eventSourceListener;
    }
}
