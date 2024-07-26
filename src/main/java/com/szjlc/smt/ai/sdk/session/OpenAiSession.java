package com.szjlc.smt.ai.sdk.session;

import com.szjlc.smt.ai.sdk.executor.parameter.*;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.concurrent.CompletableFuture;

/**
 * OpenAi 会话接口
 *
 * @author 小傅哥，微信：fustack
 */
public interface OpenAiSession {

    /**
     * 问答模式，流式反馈
     *
     * @param completionRequest   请求信息
     * @param eventSourceListener 实现监听；通过监听的 onEvent 方法接收数据
     * @return 应答结果
     * @throws Exception 异常
     */
    EventSource completions(CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception;

    /**
     * 问答模式，流式反馈 & 接收用户自定义 apiHost、apiKey - 适用于每个用户都有自己独立配置的场景
     *
     * @param apiHostByUser       apiHost
     * @param apiKeyByUser        apiKey
     * @param completionRequest   请求信息
     * @param eventSourceListener 实现监听；通过监听的 onEvent 方法接收数据
     * @return 应答结果
     * @throws Exception 异常
     */
    EventSource completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest, EventSourceListener eventSourceListener) throws Exception;

    /**
     * 问答模式，同步响应 - 对流式回答数据的同步处理
     *
     * @param completionRequest 请求信息
     * @return 应答结果
     * @throws Exception 异常
     */
    CompletableFuture<String> completions(CompletionRequest completionRequest) throws Exception;

    /**
     * 问答模式，同步响应 - 对流式回答数据的同步处理
     *
     * @param apiHostByUser     apiHost
     * @param apiKeyByUser      apiKey
     * @param completionRequest 请求信息
     * @return 应答结果
     * @throws Exception 异常
     */
    CompletableFuture<String> completions(String apiHostByUser, String apiKeyByUser, CompletionRequest completionRequest) throws Exception;

}
