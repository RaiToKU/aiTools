package com.szjlc.smt.ai.sdk.executor;

import com.szjlc.smt.ai.sdk.executor.parameter.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * @Author raito
 * @Date 2024/7/22 9:49
 * @PackageName com.szjlc.smt.ai.sdk.executor.Executor
 * @ClassName Executor
 * @Description Executor
 * @Version 1.0
 */
public interface Executor {

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
     * 生成图片
     *
     * @param imageRequest 图片描述
     * @return 应答结果
     */
    ImageResponse genImages(ImageRequest imageRequest) throws Exception;

    /**
     * 生成图片
     *
     * @param apiHostByUser apiHost
     * @param apiKeyByUser  apiKey
     * @param imageRequest  图片描述
     * @return 应答结果
     */
    ImageResponse genImages(String apiHostByUser, String apiKeyByUser, ImageRequest imageRequest) throws Exception;

    /**
     * 修改图片
     *
     * @param image            图片对象，小于4M的PNG图片
     * @param mask             图片对象，小于4M的PNG图片
     * @param imageEditRequest 图片参数
     * @return 应答结果
     */
    ImageResponse editImages(ImageEditRequest imageEditRequest) throws Exception;

    /**
     * 修改图片
     *
     * @param apiHostByUser
     * @param apiKeyByUser
     * @param imageEditRequest 图片参数
     * @return 应答结果
     */
    ImageResponse editImages(String apiHostByUser, String apiKeyByUser, ImageEditRequest imageEditRequest) throws Exception;

    /**
     * 图片理解
     * @param pictureRequest 图片和对图片的描述
     * @return 应答结果
     * @throws Exception
     */
    EventSource pictureUnderstanding(PictureRequest pictureRequest, EventSourceListener eventSourceListener) throws Exception;

    /**
     * 图片理解
     *
     * @param apiHostByUser
     * @param apiKeyByUser
     * @param pictureRequest 图片和对图片的描述
     * @return 应答结果
     * @throws Exception
     */
    EventSource pictureUnderstanding(String apiHostByUser, String apiKeyByUser, PictureRequest pictureRequest,EventSourceListener eventSourceListener) throws Exception;


}
