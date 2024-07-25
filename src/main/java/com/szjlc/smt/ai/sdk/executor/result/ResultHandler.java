package com.szjlc.smt.ai.sdk.executor.result;


import okhttp3.sse.EventSourceListener;

/**
 * @Author raito
 * @Date 2024/7/22 9:49
 * @PackageName com.szjlc.smt.ai.sdk.executor.result.ResultHandler
 * @ClassName ResultHandler
 * @Description ResultHandler
 * @Version 1.0
 */public interface ResultHandler {

    EventSourceListener eventSourceListener(EventSourceListener eventSourceListener);


}
