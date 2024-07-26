package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;

import lombok.AllArgsConstructor;

/**
 * 会话角色
 *
 * @author xs.wu
 * @className Role
 * @date 2023/12/9 12:01
 */
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

    system("system"),
    ;
    private final String code;

    public String getCode() {
        return code;
    }
}
