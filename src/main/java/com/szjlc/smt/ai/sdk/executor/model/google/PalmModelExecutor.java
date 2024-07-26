package com.szjlc.smt.ai.sdk.executor.model.google;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.google.config.PalmConfig;
import com.szjlc.smt.ai.sdk.executor.parameter.CompletionRequest;
import com.szjlc.smt.ai.sdk.executor.parameter.ImageRequest;
import com.szjlc.smt.ai.sdk.executor.parameter.ImageResponse;
import com.szjlc.smt.ai.sdk.executor.parameter.ParameterHandler;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;


/**
 * @author rsy
 * @date 2023/12/10 12:11
 */
@Slf4j
public abstract class PalmModelExecutor<T> implements Executor, ParameterHandler<T>, ResultHandler {
    /**
     * 配置信息
     */
    @Getter
    private final PalmConfig palmConfig;
    /**
     * 工厂事件
     */
    @Getter
    private final OkHttpClient okHttpClient;

    @Getter
    private final EventSource.Factory factory;

    public PalmModelExecutor(Configuration configuration) {
        this.palmConfig = configuration.getPalmConfig();
        this.okHttpClient = configuration.getOkHttpClient();
        this.factory = configuration.createRequestFactory();
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

}