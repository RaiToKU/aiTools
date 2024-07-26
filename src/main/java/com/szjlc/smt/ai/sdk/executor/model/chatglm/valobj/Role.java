package com.szjlc.smt.ai.sdk.executor.model.chatglm.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ChatGLM 对话角色
 *
 * @author 小傅哥，微信：fustack
 */
@Getter
@AllArgsConstructor
public enum Role {
    /**
     * user 用户输入的内容，role位user
     */
    user("user"),
    /**
     * 模型生成的内容，role位assistant
     */
    assistant("assistant"),
    ;
    private final String code;

}
