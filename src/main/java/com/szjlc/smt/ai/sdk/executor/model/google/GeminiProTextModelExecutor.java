package com.szjlc.smt.ai.sdk.executor.model.google;

import com.szjlc.smt.ai.sdk.executor.model.google.config.Const;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.GeminiProRole;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.model.GeminiProModel;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.model.Model;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.GeminiProTextRequest;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.TextPrompt;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.response.GeminiProCompletionResponse;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GeminiPro 模型执行器
 * <p>
 * 文档：https://ai.google.dev/tutorials/rest_quickstart#text-only_input
 * ApiKey：https://makersuite.google.com/app/apikey
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Slf4j
public class GeminiProTextModelExecutor extends GeminiProModelExecutor {

    public GeminiProTextModelExecutor(Configuration configuration) {
        super(configuration);
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // 核心参数校验；不对用户的传参做更改，只返回错误信息。
        if (completionRequest.isStream() && CompletionRequest.Model.TEXT_GEMINI_PRO.getCode().equals(completionRequest.getModel())) {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 转换参数
        GeminiProTextRequest textRequest = getParameterObject(completionRequest);

        // 获取配置
        String apiHost = apiHostByUser == null ? getGeminiProConfig().getApiHost() : apiHostByUser;
        String apiKey = apiKeyByUser == null ? getGeminiProConfig().getApiKey() : apiKeyByUser;

        Model model = GeminiProModel.getModel(completionRequest.getModel());
        Request request = new Request.Builder()
                .addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(String.format("%s%s%s:%s?key=%s", apiHost, Const.V1_BATA_GEMINI_PRO_COMPLETIONS, model.getName(),
                        model.getSupportMethod().getGenerateMethod(), apiKey))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(textRequest)))
                .build();


        Call call = getOkHttpClient().newCall(request);
        EventSource eventSource = new EventSource() {
            @Override
            public Request request() {
                return request;
            }

            @Override
            public void cancel() {
                call.cancel();
            }
        };

        call.enqueue(new ResponseCallBack(eventSourceListener, eventSource));

        return eventSource;

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
    public GeminiProTextRequest getParameterObject(CompletionRequest completionRequest) {
        List<Message> messages = completionRequest.getMessages();
        GeminiProTextRequest textRequest = new GeminiProTextRequest();
        if (messages == null || messages.isEmpty()) {
            return textRequest;
        }

        List<GeminiProTextRequest.Content> contents = messages.stream()
                .map(it -> {
                            String role = null;
                            if (CompletionRequest.Role.SYSTEM.getCode().equals(it.getRole())) {
                                role = GeminiProRole.SYSTEM.getCode();
                            } else if (CompletionRequest.Role.USER.getCode().equals(it.getRole())) {
                                role = GeminiProRole.USER.getCode();
                            }
                            GeminiProTextRequest.Content content = GeminiProTextRequest.Content.builder()
                                    .role(role)
                                    .parts(
                                            Lists.newArrayList(
                                                    new TextPrompt(it.getContent())
                                            )
                                    ).build();
                            return content;
                        }
                )
                .collect(Collectors.toList());

        textRequest.setContents(contents);

        GeminiProTextRequest.GenerationConfig generationConfig = new GeminiProTextRequest.GenerationConfig();
        generationConfig.setTemperature(completionRequest.getTemperature());
        generationConfig.setTopP(completionRequest.getTopP());
        generationConfig.setStopSequences(completionRequest.getStop());
        generationConfig.setMaxOutputTokens(completionRequest.getMaxTokens());

        textRequest.setGenerationConfig(generationConfig);
        return textRequest;
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return eventSourceListener;
    }


    private static class ResponseCallBack implements Callback {

        private final EventSourceListener eventSourceListener;
        private final EventSource eventSource;

        public ResponseCallBack(EventSourceListener eventSourceListener, EventSource eventSource) {
            this.eventSourceListener = eventSourceListener;
            this.eventSource = eventSource;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            call.cancel();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            CompletionResponse completionResponse = new CompletionResponse();
            ResponseBody body = response.body();
            List<ChatChoice> chatChoices = new ArrayList<>();
            completionResponse.setChoices(chatChoices);

            if (response.isSuccessful() && body != null) {
                List<GeminiProCompletionResponse> geminiProCompletionResponse = JSON.parseArray(body.string(), GeminiProCompletionResponse.class);

                geminiProCompletionResponse.stream()
                        .map(GeminiProCompletionResponse::getCandidates)
                        .forEach(it -> {
                            String msg = it.get(0).getContent()
                                    .getParts()
                                    .stream()
                                    .map(GeminiProCompletionResponse.Part::getText)
                                    .collect(Collectors.joining());

                            ChatChoice chatChoice;
                            chatChoice = new ChatChoice();
                            chatChoice.setDelta(Message.builder()
                                    .role(CompletionRequest.Role.SYSTEM)
                                    .name(completionResponse.getModel())
                                    .content(msg)
                                    .build());
                            chatChoices.add(chatChoice);
                        });

                completionResponse.setChoices(chatChoices);

            }
            ChatChoice chatChoice = new ChatChoice();
            chatChoice.setFinishReason("stop");
            chatChoice.setDelta(new Message());
            chatChoices.add(chatChoice);
            eventSourceListener.onEvent(eventSource, null, null, JSON.toJSONString(completionResponse));
        }
    }

}
