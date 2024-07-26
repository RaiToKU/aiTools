package com.szjlc.smt.ai.sdk.executor.model.google;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.google.config.GeminiProConfig;
import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.GeminiProTextRequest;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * GeminiPro 模型执行器
 * <p>
 * 文档：https://ai.google.dev/tutorials/rest_quickstart#text-only_input
 * ApiKey：https://makersuite.google.com/app/apikey
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Slf4j
public abstract class GeminiProModelExecutor implements Executor, ParameterHandler<GeminiProTextRequest>, ResultHandler {

    /**
     * 配置信息
     */
    @Getter
    private  GeminiProConfig geminiProConfig;


    @Getter
    private final OkHttpClient okHttpClient;


    /**
     * 工厂事件
     */
    @Getter
    private final EventSource.Factory factory;

    public GeminiProModelExecutor(Configuration configuration) {
//        this.geminiProConfig = configuration.getGeminiProConfig();
        this.factory = configuration.createRequestFactory();
        this.okHttpClient = configuration.getOkHttpClient();
    }

    @Override
    public EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        return completions(null, null, completionRequest, eventSourceListener);
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
    public EventSource pictureUnderstanding(PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
    }

    @Override
    public EventSource pictureUnderstanding(String apiHostByUser, String apiKeyByUser, PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception {
        return null;
    }
}
