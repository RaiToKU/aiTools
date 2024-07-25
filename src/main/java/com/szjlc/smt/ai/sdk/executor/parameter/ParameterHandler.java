package com.szjlc.smt.ai.sdk.executor.parameter;

/**
 * 参数处理器
 *
 * @author 小傅哥，微信：fustack
 */
public interface ParameterHandler<T> {

    T getParameterObject(CompletionRequest completionRequest);

}
