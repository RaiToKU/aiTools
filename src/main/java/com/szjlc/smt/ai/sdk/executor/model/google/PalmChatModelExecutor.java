package com.szjlc.smt.ai.sdk.executor.model.google;

import com.szjlc.smt.ai.sdk.executor.model.google.config.Const;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.Role;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.model.Model;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.model.PalmModel;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.MessagePrompt;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.PalmChatRequest;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.PalmMessage;
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

public class PalmChatModelExecutor extends PalmModelExecutor<PalmChatRequest> {

    public PalmChatModelExecutor(Configuration configuration) {
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

    private Request getRequest(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest) throws JsonProcessingException {
        String apiHost = apiHostByUser == null || apiHostByUser.length() == 0 ? getPalmConfig().getApiHost() : apiHostByUser;
        String apiKey = apiKeyByUser == null || apiKeyByUser.length() == 0 ? getPalmConfig().getApiKey() : apiKeyByUser;
        // 设置请求数据
        Model model = PalmModel.getModel(completionRequest.getModel());
        if (model == null) {
            throw new RuntimeException("model must not be null!");
        }
        PalmChatRequest palmChatRequest = getParameterObject(completionRequest);
        return new Request.Builder().addHeader("Content-Type", Configuration.APPLICATION_JSON)
                .url(String.format("%s%s%s:%s?key=%s", apiHost, Const.v2_completions, model.getName(),
                        model.getSupportMethod().getGenerateMethod(), apiKey))
                .post(RequestBody.create(MediaType.parse(Configuration.APPLICATION_JSON), new ObjectMapper().writeValueAsString(palmChatRequest)))
                .build();
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
            List<ChatChoice> chatChoices = new ArrayList<>();
            completionResponse.setChoices(chatChoices);
            ResponseBody body = response.body();
            if (response.isSuccessful() && body != null) {
                PalmCompletionResponse palmCompletionResponse = JSON.parseObject(body.string(), PalmCompletionResponse.class);
                ChatChoice chatChoice;
                List<Candidate> candidates = palmCompletionResponse.getCandidates();
                for (Candidate candidate : candidates) {
                    chatChoice = new ChatChoice();
                    chatChoice.setDelta(Message.builder()
                            .role(CompletionRequest.Role.SYSTEM)
                            .name(completionResponse.getModel())
                            .content(candidate.getContent())
                            .build());
                    chatChoices.add(chatChoice);
                }
                completionResponse.setChoices(chatChoices);
            }
            ChatChoice chatChoice = new ChatChoice();
            chatChoice.setDelta(new Message());
            chatChoice.setFinishReason("stop");
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
    public EventSource pictureUnderstanding(PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
    }

    @Override
    public EventSource pictureUnderstanding(String apiHostByUser, String apiKeyByUser, PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
    }

    @Override
    public PalmChatRequest getParameterObject(CompletionRequest completionRequest) {
        // 转换参数
        List<Message> messages = completionRequest.getMessages();
        List<PalmMessage> palmMessageList = new ArrayList<>(messages.size());
        PalmMessage palmMessage;
        for (Message message : messages) {
            palmMessage = new PalmMessage();
            palmMessage.setContent(message.getContent());
            if (CompletionRequest.Role.SYSTEM.equals(message.getRole())) {
                palmMessage.setAuthor(Role.SYSTEM.getCode());
            } else if (CompletionRequest.Role.USER.equals(message.getRole())) {
                palmMessage.setAuthor(Role.USER.getCode());
            }
            palmMessageList.add(palmMessage);
        }
        // 封装参数
        PalmChatRequest palmChatRequest = new PalmChatRequest();
        palmChatRequest.setTemperature(completionRequest.getTemperature());
        palmChatRequest.setTopP(completionRequest.getTopP());
        MessagePrompt messagePrompt = new MessagePrompt();
        messagePrompt.setMessages(palmMessageList);
        palmChatRequest.setPrompt(messagePrompt);
        return palmChatRequest;
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        return eventSourceListener;
    }


}
