package com.szjlc.smt.ai.sdk.executor.model.chatglm.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ChatGLM 消息模型
 *
 * @author 小傅哥，微信：fustack
 */
@Getter
@AllArgsConstructor
public enum Model {

    CHATGLM_6B_SSE("chatGLM_6b_SSE", "ChatGLM-6B 测试模型"),
    CHATGLM_LITE("chatglm_lite", "轻量版模型，适用对推理速度和成本敏感的场景"),
    CHATGLM_LITE_32K("chatglm_lite_32k", "标准版模型，适用兼顾效果和成本的场景"),
    CHATGLM_STD("chatglm_std", "适用于对知识量、推理能力、创造力要求较高的场景"),
    CHATGLM_PRO("chatglm_pro", "适用于对知识量、推理能力、创造力要求较高的场景"),

    ;
    private final String code;
    private final String info;
}
