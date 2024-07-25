package com.szjlc.smt.ai.sdk.executor.model.claude;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.claude.config.ClaudeConfig;
import com.szjlc.smt.ai.sdk.executor.model.claude.valobj.ClaudeCompletionRequest;
import com.szjlc.smt.ai.sdk.executor.parameter.*;
import com.szjlc.smt.ai.sdk.executor.result.ResultHandler;
import com.szjlc.smt.ai.sdk.session.Configuration;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * Claude 模型执行器
 * <p>
 * 文档：https://open.bigmodel.cn/dev/api#chatglm_turbo
 * ApiKey：https://open.bigmodel.cn/usercenter/apikeys
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Slf4j
public class ClaudeModelExecutor implements Executor, ParameterHandler<ClaudeCompletionRequest>, ResultHandler {

    /**
     * 配置信息
     */
    private final ClaudeConfig claudeConfig;
    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    public ClaudeModelExecutor(Configuration configuration) {
        this.claudeConfig = configuration.getClaudeConfig();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // TODO: 2023/12/14 未实现
        return null;
    }

    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception {
        // TODO: 2023/12/14 未实现
        return null;
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
    public ClaudeCompletionRequest getParameterObject(CompletionRequest completionRequest) {
        // TODO: 2023/12/14 未实现
        return null;
    }

    @Override
    public EventSourceListener eventSourceListener(EventSourceListener eventSourceListener) {
        // TODO: 2023/12/14 未实现
        return null;
    }

}
