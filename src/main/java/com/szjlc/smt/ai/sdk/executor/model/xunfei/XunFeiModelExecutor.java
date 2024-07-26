package com.szjlc.smt.ai.sdk.executor.model.xunfei;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.config.XunFeiConfig;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.utils.URLAuthUtils;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj.*;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj.Usage;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.parameter.Message;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 讯飞大模型 https://console.xfyun.cn/services/bm3
 *
 * @author 小傅哥，微信：fustack
 */
@Slf4j
public class XunFeiModelExecutor implements Executor, ParameterHandler<XunFeiCompletionRequest> {

    /**
     * 配置信息
     */
    private final XunFeiConfig xunFeiConfig;
    /**
     * 客户端
     */
    private final OkHttpClient okHttpClient;

    private String appid;

    public XunFeiModelExecutor(Configuration configuration) {
        this.xunFeiConfig = configuration.getXunFeiConfig();
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
        String apiHost = null == apiHostByUser ? xunFeiConfig.getApiHost() : apiHostByUser;
        String authURL = getAuthURL(apiKeyByUser, apiHost);

        // 1. 转换参数信息
        XunFeiCompletionRequest xunFeiCompletionRequest = getParameterObject(completionRequest);
        // 2. 构建请求信息
        Request request = new Request.Builder()
                .url(authURL)
                .build();

        // 3. 调用请求
        WebSocket webSocket = okHttpClient.newWebSocket(request, new BigModelWebSocketListener(xunFeiCompletionRequest, eventSourceListener));
        // 4. 封装结果
        return new EventSource() {

            @NotNull
            @Override
            public Request request() {
                return request;
            }

            @Override
            public void cancel() {
                webSocket.cancel();
            }
        };
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest) throws Exception {
        return genImages(null, null, imageRequest);
    }

    @Override
    public ImageResponse genImages(String apiHostByUser, String apiKeyByUser, ImageRequest imageRequest) throws Exception {

        // 1. 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = null == apiHostByUser ? xunFeiConfig.getApiTtiHost() : apiHostByUser;
        String authURL = getAuthURL(apiKeyByUser,apiHost,"POST", Boolean.FALSE);

        CompletionRequest completionRequest = CompletionRequest.builder()
                .messages(Collections.singletonList(Message.builder().role(CompletionRequest.Role.USER).content(imageRequest.getPrompt()).build()))
                .temperature(0.5)
                .build();
        // 1. 转换参数信息
        XunFeiCompletionRequest xunFeiCompletionRequest = getParameterObject(completionRequest, "general");
        // 2. 构建请求信息
        Request request = new Request.Builder()
                .url(authURL)
                .post(RequestBody.create(MediaType.parse("POST"), JSON.toJSONString(xunFeiCompletionRequest)))
                .build();
        //3. 执行请求
        Response execute = okHttpClient.newCall(request).execute();
        if (execute.isSuccessful() && execute.body() != null) {
            XunFeiCompletionResponse xunFeiGenImageResponse = JSON.parseObject(execute.body().string(), XunFeiCompletionResponse.class);
            if (xunFeiGenImageResponse.getHeader().getCode() == XunFeiCompletionResponse.Header.Code.SUCCESS.getValue()) {
                XunFeiCompletionResponse.Payload payload = xunFeiGenImageResponse.getPayload();

                List<Text> texts = payload.getChoices().getText();
                ImageResponse imageResponse = new ImageResponse();
                imageResponse.setCreated(System.currentTimeMillis());
                imageResponse.setData(new ArrayList<>());
                for (Text text : texts) {
                    Item item = new Item();
                    item.setUrl(base64ToImageUrl(text.getContent()));
                    imageResponse.getData().add(item);
                }
                return imageResponse;

            } else {
                log.error("生成图片失败，code:{},message:{}", xunFeiGenImageResponse.getHeader().getCode(), xunFeiGenImageResponse.getHeader().getMessage());
            }
        }

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
        return pictureUnderstanding(null, null, pictureRequest, eventSourceListener);
    }

    @Override
    public EventSource pictureUnderstanding(String apiHostByUser, String apiKeyByUser, PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {

        String apiHost = null == apiHostByUser ? xunFeiConfig.getApiPictureHost() : apiHostByUser;
        String authURL = getAuthURL(apiKeyByUser, apiHost);
        // 1. 转换参数信息
        XunFeiCompletionRequest xunFeiCompletionRequest = getParameterObject(pictureRequest);
        // 2. 构建请求信息
        Request request = new Request.Builder()
                .url(authURL)
                .build();

        WebSocket webSocket = okHttpClient.newWebSocket(request, new BigModelWebSocketListener(xunFeiCompletionRequest, eventSourceListener));

        return new EventSource() {
            @Override
            public Request request() {
                return request;
            }

            @Override
            public void cancel() {
                webSocket.cancel();
            }
        };
    }

    private String getAuthURL(String apiKeyByUser, String apiHost) throws Exception {

        return getAuthURL(apiKeyByUser,apiHost,"GET",Boolean.TRUE);
    }

    private String getAuthURL(String apiKeyByUser, String apiHost,String httpMethod, Boolean websocket) throws Exception {
        String authURL;
        if (apiKeyByUser == null) {
            authURL = URLAuthUtils.getAuthURl(apiHost, xunFeiConfig.getApiKey(), xunFeiConfig.getApiSecret(),httpMethod,websocket);
            appid = xunFeiConfig.getAppid();
        } else {
            // 拆解 879d40fc.fe81b961ccb561c404f844838fa09876.MjUzYTdhMWEyNThiZDBhMTE1NmRjZTk3
            String[] configs = apiKeyByUser.split(".");
            appid = configs[0];
            authURL = URLAuthUtils.getAuthURl(apiHost, configs[1], configs[2],httpMethod,websocket);
        }
        return authURL;
    }

    private String base64ToImageUrl(String base64String) {
        // 将Base64编码字符串转换为字节数组
        byte[] imageBytes = Base64.getDecoder().decode(base64String.split(",")[1]);

        // 将字节数组转换为图像URL
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    protected static class BigModelWebSocketListener extends WebSocketListener {

        private final XunFeiCompletionRequest request;
        private final EventSourceListener eventSourceListener;
        private final CountDownLatch countDownLatch = new CountDownLatch(1);
        private final EventSource eventSource;

        public BigModelWebSocketListener(XunFeiCompletionRequest request, EventSourceListener eventSourceListener) {
            this.request = request;
            this.eventSourceListener = eventSourceListener;
            this.eventSource = new EventSource() {

                @Override
                public Request request() {
                    return this.request();
                }

                @Override
                public void cancel() {
                    this.cancel();
                }
            };
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            new Thread(() -> {
                webSocket.send(JSON.toJSONString(request));
                // 等待服务端返回完毕后关闭
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                webSocket.close(1000, "");
            }).start();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            XunFeiCompletionResponse response = JSON.parseObject(text, XunFeiCompletionResponse.class);
            XunFeiCompletionResponse.Header header = response.getHeader();
            int code = header.getCode();

            // 反馈失败
            if (XunFeiCompletionResponse.Header.Code.SUCCESS.getValue() != code) {
                countDownLatch.countDown();
                return;
            }

            // 封装参数
            CompletionResponse completionResponse = new CompletionResponse();
            List<ChatChoice> chatChoices = new ArrayList<>();
            ChatChoice chatChoice = new ChatChoice();

            XunFeiCompletionResponse.Payload payload = response.getPayload();
            Choices choices = payload.getChoices();
            List<Text> texts = choices.getText();

            for (Text t : texts) {
                chatChoice.setDelta(com.szjlc.smt.ai.sdk.executor.parameter.Message.builder()
                        .name("")
                        .role(CompletionRequest.Role.SYSTEM)
                        .content(t.getContent())
                        .build());
                chatChoices.add(chatChoice);
            }
            completionResponse.setChoices(chatChoices);

            int status = header.getStatus();
            if (XunFeiCompletionResponse.Header.Status.START.getValue() == status) {
                eventSourceListener.onEvent(eventSource, null, null, JSON.toJSONString(completionResponse));
            } else if (XunFeiCompletionResponse.Header.Status.ING.getValue() == status) {
                eventSourceListener.onEvent(eventSource, null, null, JSON.toJSONString(completionResponse));
            } else if (XunFeiCompletionResponse.Header.Status.END.getValue() == status) {
                Usage usage = payload.getUsage();
                Usage.Text usageText = usage.getText();
                com.szjlc.smt.ai.sdk.executor.parameter.Usage openaiUsage = new com.szjlc.smt.ai.sdk.executor.parameter.Usage();
                openaiUsage.setPromptTokens(usageText.getPromptTokens());
                openaiUsage.setCompletionTokens(usageText.getCompletionTokens());
                openaiUsage.setTotalTokens(usageText.getTotalTokens());
                completionResponse.setUsage(openaiUsage);
                completionResponse.setCreated(System.currentTimeMillis());
                chatChoice.setFinishReason("stop");
                chatChoices.add(chatChoice);
                eventSourceListener.onEvent(eventSource, null, null, JSON.toJSONString(completionResponse));
                countDownLatch.countDown();
            }


        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            eventSourceListener.onClosed(eventSource);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
            eventSourceListener.onFailure(eventSource, t, response);
        }
    }

    private XunFeiCompletionRequest getParameterObject(CompletionRequest completionRequest, String domain) {
        // 头信息
        XunFeiCompletionRequest.Header header = getHeader();
        // 模型
        XunFeiCompletionRequest.Parameter parameter = XunFeiCompletionRequest.Parameter.builder().chat(Chat.builder()
                .domain(domain)
                .temperature(completionRequest.getTemperature())
                .maxTokens(completionRequest.getMaxTokens())
                .build()).build();
        // 内容
        List<Text> texts = new ArrayList<>();
        List<com.szjlc.smt.ai.sdk.executor.parameter.Message> messages = completionRequest.getMessages();
        for (com.szjlc.smt.ai.sdk.executor.parameter.Message message : messages) {
            texts.add(Text.builder()
                    .role(Text.Role.USER.getName())
                    .content(message.getContent())
                    .build());
        }

        XunFeiCompletionRequest.Payload payload = XunFeiCompletionRequest.Payload.builder()
                .message(com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj.Message.builder().text(texts).build())
                .build();

        return XunFeiCompletionRequest.builder()
                .header(header)
                .parameter(parameter)
                .payload(payload)
                .build();
    }

    @Override
    public XunFeiCompletionRequest getParameterObject(CompletionRequest completionRequest) {
        return getParameterObject(completionRequest, "generalv2");
    }

    private XunFeiCompletionRequest getParameterObject(PictureRequest pictureRequest) {
        // 头信息
        XunFeiCompletionRequest.Header header = getHeader();
        // 模型
        XunFeiCompletionRequest.Parameter parameter = XunFeiCompletionRequest.Parameter.builder().chat(Chat.builder()
                .domain("general")
                .temperature(pictureRequest.getTemperature())
                .maxTokens(pictureRequest.getMaxTokens())
                .auditing("default")
                .build()).build();
        // 内容
        List<Text> texts = new ArrayList<>();
        List<PictureRequest.Text> messages = pictureRequest.getMessages();
        for (PictureRequest.Text message : messages) {
            texts.add(Text.builder()
                    .role(Text.Role.USER.getName())
                    .content(message.getContent())
                    .contentType(message.getContentType())
                    .build());
        }

        XunFeiCompletionRequest.Payload payload = XunFeiCompletionRequest.Payload.builder()
                .message(com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj.Message.builder().text(texts).build())
                .build();

        return XunFeiCompletionRequest.builder()
                .header(header)
                .parameter(parameter)
                .payload(payload)
                .build();
    }

    private XunFeiCompletionRequest.Header getHeader() {
        XunFeiCompletionRequest.Header header = XunFeiCompletionRequest.Header.builder()
                .appid(xunFeiConfig.getAppid())
                .uid(UUID.randomUUID().toString().substring(0, 10))
                .build();
        return header;
    }

}
