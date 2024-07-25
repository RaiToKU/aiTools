package com.szjlc.smt.ai.sdk.executor.model.chatgpt;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.chatgpt.config.ChatGPTConfig;
import com.szjlc.smt.ai.sdk.executor.model.chatgpt.valobj.ChatGPTCompletionRequest;
import com.szjlc.smt.ai.sdk.executor.model.chatgpt.valobj.ChatGPTImageRequest;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ChatGPT 模型执行器 https://openai.apifox.cn/doc-3222729
 *
 * @author 小傅哥，微信：fustack
 */
public class ChatGPTModelExecutor implements Executor, ParameterHandler<ChatGPTCompletionRequest>, ResultHandler {

    /**
     * 配置信息
     */
    private final ChatGPTConfig chatGPTConfig;
    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;
    /**
     * http 客户端
     */
    private final OkHttpClient okHttpClient;

    public ChatGPTModelExecutor(Configuration configuration) {
        this.chatGPTConfig = configuration.getChatGPTConfig();
        this.factory = configuration.createRequestFactory();
        this.okHttpClient = configuration.getOkHttpClient();
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
        String apiHost = null == apiHostByUser ? chatGPTConfig.getApiHost() : apiHostByUser;
        String apiKey = null == apiKeyByUser ? chatGPTConfig.getApiKey() : apiKeyByUser;

        // 3. 转换参数信息
        ChatGPTCompletionRequest chatGPTCompletionRequest = getParameterObject(completionRequest);

        // 4. 构建请求信息
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + apiKey)
                .url(apiHost.concat(chatGPTConfig.getV1_chat_completions()))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(chatGPTCompletionRequest)))
                .build();

        // 5. 返回事件结果
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest) throws Exception {
        return genImages(null, null, imageRequest);
    }

    @Override
    public ImageResponse genImages(String apiHostByUser, String apiKeyByUser, ImageRequest imageRequest) throws Exception {
        // 1. 统一转换参数
        ChatGPTImageRequest chatGPTImageRequest = ChatGPTImageRequest.builder()
                .n(imageRequest.getN())
                .size(imageRequest.getSize())
                .prompt(imageRequest.getPrompt())
                .model(imageRequest.getModel())
                .build();

        // 2. 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? chatGPTConfig.getApiHost() : apiHostByUser;
        String apiKey = null == apiKeyByUser ? chatGPTConfig.getApiKey() : apiKeyByUser;

        // 构建请求信息
        Request request = new Request.Builder()
                // url: https://api.openai.com/v1/chat/completions - 通过 IOpenAiApi 配置的 POST 接口，用这样的方式从统一的地方获取配置信息
                .url(apiHost.concat(chatGPTConfig.getV1_images_completions()))
                .header("Authorization", "Bearer " + apiKey)
                // 封装请求参数信息，如果使用了 Fastjson 也可以替换 ObjectMapper 转换对象
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(chatGPTImageRequest)))
                .build();

        Call call = okHttpClient.newCall(request);
        Response execute = call.execute();
        ResponseBody body = execute.body();

        if (execute.isSuccessful() && body != null) {
            String responseBody = body.string();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, ImageResponse.class);
        } else {
            throw new IOException("Failed to get image response");
        }

    }

    @Override
    public ImageResponse editImages(ImageEditRequest imageEditRequest) throws Exception {
        return editImages(null, null, imageEditRequest);
    }

    @Override
    public ImageResponse editImages(String apiHostByUser, String apiKeyByUser, ImageEditRequest imageEditRequest) throws Exception {
        // 1. 请求体
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        // 添加文件字段image
        requestBodyBuilder.addFormDataPart("image", imageEditRequest.getImage().getName(),
                RequestBody.create(MediaType.parse("image/png"), imageEditRequest.getImage()));

        // 添加文件字段mask
        if (null != imageEditRequest.getMask()) requestBodyBuilder.addFormDataPart("mask", imageEditRequest.getMask().getName(),
                RequestBody.create(MediaType.parse("image/png"), imageEditRequest.getMask()));

        requestBodyBuilder.addFormDataPart("prompt", imageEditRequest.getPrompt());
        requestBodyBuilder.addFormDataPart("model", imageEditRequest.getModel());
        requestBodyBuilder.addFormDataPart("n", String.valueOf(imageEditRequest.getN()));
        requestBodyBuilder.addFormDataPart("size", imageEditRequest.getSize());
        requestBodyBuilder.addFormDataPart("response_format", imageEditRequest.getResponseFormat());
        if (!(Objects.isNull(imageEditRequest.getUser()) || "".equals(imageEditRequest.getUser()))) {
            requestBodyBuilder.addFormDataPart("user", imageEditRequest.getUser());
        }

        // 2. 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? chatGPTConfig.getApiHost() : apiHostByUser;
        String apiKey = null == apiKeyByUser ? chatGPTConfig.getApiKey() : apiKeyByUser;

        // 3. 构建请求
        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + apiKey)
                .url(apiHost.concat(chatGPTConfig.getV1_images_edits()))
                .post(requestBodyBuilder.build())
                .build();

        Call call = okHttpClient.newCall(request);
        Response execute = call.execute();
        ResponseBody body = execute.body();

        if (execute.isSuccessful() && body != null) {
            String responseBody = body.string();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, ImageResponse.class);
        } else {
            throw new IOException("Failed to edit image response");
        }
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
    public ChatGPTCompletionRequest getParameterObject(CompletionRequest completionRequest) {
        // 转换参数
        List<com.szjlc.smt.ai.sdk.executor.model.chatgpt.valobj.Message> chatGPTMessages = new ArrayList<>();
        List<Message> messages = completionRequest.getMessages();
        for (Message message : messages) {
            com.szjlc.smt.ai.sdk.executor.model.chatgpt.valobj.Message messageVO = new com.szjlc.smt.ai.sdk.executor.model.chatgpt.valobj.Message();
            messageVO.setContent(message.getContent());
            messageVO.setName(message.getName());
            messageVO.setRole(message.getRole());
            chatGPTMessages.add(messageVO);
        }

        // 封装参数
        ChatGPTCompletionRequest chatGPTCompletionRequest = new ChatGPTCompletionRequest();
        chatGPTCompletionRequest.setModel(completionRequest.getModel());
        chatGPTCompletionRequest.setTemperature(completionRequest.getTemperature());
        chatGPTCompletionRequest.setTopP(completionRequest.getTopP());
        chatGPTCompletionRequest.setStream(completionRequest.isStream());
        chatGPTCompletionRequest.setMessages(chatGPTMessages);

        return chatGPTCompletionRequest;
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return eventSourceListener;
    }

}
