package com.szjlc.smt.ai.sdk.executor.model.google;

import com.szjlc.smt.ai.sdk.executor.model.google.config.Const;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.model.Model;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.model.PalmModel;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.PalmTextRequest;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.TextPrompt;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.response.Candidate;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.response.PalmCompletionResponse;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 文本模型执行器
 */
public class PalmTextModelExecutor extends PalmModelExecutor<PalmTextRequest> {
    public PalmTextModelExecutor(Configuration configuration) {
        super(configuration);
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        Request request = getRequest(apiHostByUser, apiKeyByUser, completionRequest);
        // 请求
        Call call = getOkHttpClient().newCall(request);
        call.enqueue(new ResponseCallBack(eventSourceListener));
        return new EventSource() {
            @Override
            public Request request() {
                return request;
            }

            @Override
            public void cancel() {
                call.cancel();
            }
        };
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

    private Request getRequest(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest) throws JsonProcessingException {
        String apiHost = apiHostByUser == null || apiHostByUser.length() == 0 ? getPalmConfig().getApiHost() : apiHostByUser;
        String apiKey = apiKeyByUser == null || apiKeyByUser.length() == 0 ? getPalmConfig().getApiKey() : apiKeyByUser;
        // 设置请求数据
        Model model = PalmModel.getModel(completionRequest.getModel());
        if (model == null) {
            throw new RuntimeException("model must not be null!");
        }
        PalmTextRequest palmTextRequest = getParameterObject(completionRequest);
        return new Request.Builder().addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(String.format("%s%s%s:%s?key=%s", apiHost, Const.v2_completions, model.getName(),
                        model.getSupportMethod().getGenerateMethod(), apiKey))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(palmTextRequest)))
                .build();
    }

    @Override
    public PalmTextRequest getParameterObject(CompletionRequest completionRequest) {
        // 转换参数
        List<Message> messages = completionRequest.getMessages();
        PalmTextRequest request = new PalmTextRequest();
        if (messages == null || messages.size() == 0) {
            return request;
        }
        TextPrompt textPrompt = new TextPrompt();
        textPrompt.setText(messages.get(0).getContent());
        // 封装参数
        request.setTemperature(completionRequest.getTemperature());
        request.setTopP(completionRequest.getTopP());
        request.setPrompt(textPrompt);
        return request;
    }

    private static class ResponseCallBack implements Callback {

        private final EventSourceListener eventSourceListener;

        public ResponseCallBack(EventSourceListener eventSourceListener) {
            this.eventSourceListener = eventSourceListener;
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
                PalmCompletionResponse palmCompletionResponse = JSON.parseObject(body.string(), PalmCompletionResponse.class);
                ChatChoice chatChoice;
                List<Candidate> candidates = palmCompletionResponse.getCandidates();
                for (Candidate candidate : candidates) {
                    chatChoice = new ChatChoice();
                    chatChoice.setDelta(Message.builder()
                            .role(CompletionRequest.Role.SYSTEM)
                            .name(completionResponse.getModel())
                            .content(candidate.getOutput())
                            .build());

                    chatChoices.add(chatChoice);
                }
            }
            ChatChoice chatChoice = new ChatChoice();
            chatChoice.setFinishReason("stop");
            chatChoice.setDelta(new Message());
            chatChoices.add(chatChoice);
            eventSourceListener.onEvent(new EventSource() {
                @Override
                public Request request() {
                    return null;
                }

                @Override
                public void cancel() {

                }
            }, null, null, JSON.toJSONString(completionResponse));
        }
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return eventSourceListener;
    }
}
