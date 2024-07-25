package com.szjlc.smt.ai.sdk.executor.model.gemini;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.gemini.config.GeminiProConfig;
import com.szjlc.smt.ai.sdk.executor.model.gemini.valobj.*;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj.Text;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okio.BufferedSource;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author AZ
 * @Description Gemini Pro执行器
 * @creat 2023/12/17 13:35
 */
@Slf4j
public class GeminiProModelExecutor implements Executor, ParameterHandler<GeminiProCompletionRequest> ,ResultHandler{

    /**
     * 配置信息
     */
    private final GeminiProConfig geminiProConfig;


    /**
     * http客户端
     */
    private final OkHttpClient okHttpClient;

    public GeminiProModelExecutor(Configuration configuration) {
        this.geminiProConfig = configuration.getGeminiProConfig();
        this.okHttpClient=configuration.getOkHttpClient();
    }

    @Override
    public EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        return completions(null,null,completionRequest,eventSourceListener);
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // 1. 核心参数校验；不对用户的传参做更改，只返回错误信息。
        if (!completionRequest.isStream()) {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 2. 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? geminiProConfig.getApiHost() : apiHostByUser;
        String apiKey = null == apiKeyByUser ? geminiProConfig.getApiKey() : apiKeyByUser;

        // 3. 转换参数信息
        GeminiProCompletionRequest geminiProCompletionRequest = getParameterObject(completionRequest);

        // 4. 构建请求信息
        Request request = new Request.Builder()
                .addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(apiHost.concat(geminiProConfig.getV1beta_chat_completions())
                        .concat(Model.valueOf(completionRequest.getModel().toUpperCase()).getPath()).concat("?key="+apiKey))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(geminiProCompletionRequest)))
                .build();

        // 5. 异步执行 POST 请求
        EventSource eventSource = new EventSource() {
            @NotNull
            @Override
            public Request request() {
                return request;
            }

            @Override
            public void cancel() {
                log.info("请求取消");
            }
        };
        //执行请求
        excuteRequest(okHttpClient,request,eventSource,eventSourceListener);
        // 6. 封装结果
        return eventSource;
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return eventSourceListener;
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest) throws Exception {
        return null;
    }

    @Override
    public ImageResponse genImages(String apiHostByUser, String apiKeyByUser, ImageRequest imageRequest) throws Exception {
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
        return pictureUnderstanding(null,null,pictureRequest,eventSourceListener);
    }

    @Override
    public EventSource pictureUnderstanding(String apiHostByUser, String apiKeyByUser, PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        // 1. 核心参数校验；不对用户的传参做更改，只返回错误信息。
        if (!pictureRequest.isStream()) {
            throw new RuntimeException("illegal parameter stream is false!");
        }
        //模型不对
        if (!pictureRequest.getModel().equals(Model.GEMINI_PRO_VERSION.getCode())){
            throw new RuntimeException("Model is error");
        }

        // 2. 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? geminiProConfig.getApiHost() : apiHostByUser;
        String apiKey = null == apiKeyByUser ? geminiProConfig.getApiKey() : apiKeyByUser;

        // 3. 转换参数信息
        GeminiProCompletionRequest geminiProCompletionRequest = getParameterObject(pictureRequest);

        // 4. 构建请求信息
        Request request = new Request.Builder()
                .addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(apiHost.concat(geminiProConfig.getV1beta_chat_completions())
                        .concat(Model.valueOf(pictureRequest.getModel().toUpperCase()).getPath()).concat("?key="+apiKey))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(geminiProCompletionRequest)))
                .build();

        // 5. 异步执行 POST 请求
        EventSource eventSource = new EventSource() {
            @NotNull
            @Override
            public Request request() {
                return request;
            }

            @Override
            public void cancel() {
                log.info("请求取消");
            }
        };
        //执行请求
        excuteRequest(okHttpClient,request,eventSource,eventSourceListener);
        // 6. 封装结果
        return eventSource;
    }

    @Override
    public GeminiProCompletionRequest getParameterObject(CompletionRequest completionRequest) {
        GeminiProCompletionRequest geminiProCompletionRequest=new GeminiProCompletionRequest();
        List<Content> contents=new ArrayList<>();
        List<Message> messages = completionRequest.getMessages();
        for (Message message : messages) {
            //若角色为"user"则是用户，否则为模型
            String role = message.getRole().equals(CompletionRequest.Role.USER.getCode()) ? Role.user.getCode() : Role.model.getCode();
            //构建消息内容
            List<Parts> parts = Collections.singletonList(Parts.builder().text(message.getContent()).build());
            Content content=Content.builder()
                    .role(role)
                    .parts(parts).build();
            //加入消息列表
            contents.add(content);
        }
        //设置配置信息
        GenerationConfig generationConfig=GenerationConfig.builder()
                .temperature(completionRequest.getTemperature())
                .topP(completionRequest.getTopP())
                .maxOutputTokens(completionRequest.getMaxTokens())
                .stopSequences(completionRequest.getStop()).build();

        geminiProCompletionRequest.setContents(contents);
        geminiProCompletionRequest.setGenerationConfig(generationConfig);
        geminiProCompletionRequest.setSafetySettings(Collections.singletonList(new SafetySetting()));
        return geminiProCompletionRequest;
    }

    public GeminiProCompletionRequest getParameterObject(PictureRequest pictureRequest) {
        //图片理解不支持多轮对话
        GeminiProCompletionRequest geminiProCompletionRequest=new GeminiProCompletionRequest();
        List<Content> contents=new ArrayList<>();
        List<PictureRequest.Text> messages = pictureRequest.getMessages();
        List<Parts> parts = new ArrayList<>();
        for (PictureRequest.Text message : messages) {
            //构建消息内容
            if (message.getContentType().equals(PictureContentEnum.ContentType.TEXT.getValue())){
                parts.add(Parts.builder().text(message.getContent()).build());
            }if (message.getContentType().equals(PictureContentEnum.ContentType.IMAGE.getValue())){
                parts.add(Parts.builder().inlineData(InlineData.builder().imgData(message.getContent()).build()).build());
            }
        }
        Content content=Content.builder()
                .role(Role.user.getCode())
                .parts(parts).build();
        //加入消息列表
        contents.add(content);
        //设置配置信息
        GenerationConfig generationConfig=GenerationConfig.builder()
                .temperature(pictureRequest.getTemperature())
                .maxOutputTokens(pictureRequest.getMaxTokens())
                .stopSequences(pictureRequest.getStop()).build();

        geminiProCompletionRequest.setContents(contents);
        geminiProCompletionRequest.setGenerationConfig(generationConfig);
        geminiProCompletionRequest.setSafetySettings(Collections.singletonList(new SafetySetting()));
        return geminiProCompletionRequest;
    }

    private void excuteRequest(OkHttpClient okHttpClient,Request request,EventSource eventSource,EventSourceListener eventSourceListener){
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // 处理请求失败
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 获取流式的 ResponseBody
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try (BufferedSource source = responseBody.source()) {
                            // 逐段读取数据
                            try {
                                while (!source.exhausted()) {
                                    String data = source.readUtf8();
                                    // 封装参数
                                    CompletionResponse completionResponse = new CompletionResponse();
                                    List<ChatChoice> choices = new ArrayList<>();
                                    List<GeminiProCompletionResponse> geminiCompletionResponses = JSON.parseArray(data, GeminiProCompletionResponse.class);
                                    for (GeminiProCompletionResponse geminiCompletionResponse : geminiCompletionResponses) {
                                        Candidate candidate = geminiCompletionResponse.getCandidates().get(0);
                                        Content content = candidate.getContent();
                                        if (ObjectUtils.isNotEmpty(content)){
                                            String finishReason = candidate.getFinishReason();
                                            ChatChoice chatChoice = new ChatChoice();
                                            chatChoice.setDelta(com.szjlc.smt.ai.sdk.executor.parameter.Message.builder()
                                                    .name("")
                                                    .role(CompletionRequest.Role.MODEL)
                                                    .content(content.getParts().get(0).getText())
                                                    .build());
                                            chatChoice.setFinishReason(finishReason);
                                            choices.add(chatChoice);
                                        }
                                    }
                                    completionResponse.setChoices(choices);
                                    eventSourceListener.onEvent(eventSource,null,null,JSON.toJSONString(completionResponse));
                                }
                            }finally {
                                source.close();
                                eventSourceListener.onClosed(eventSource);
                            }
                        }
                    }
                } else {
                    // 处理请求失败
                    System.out.println("Request failed. Response code: " + response.code());
                }
            }
        });
    }

}
