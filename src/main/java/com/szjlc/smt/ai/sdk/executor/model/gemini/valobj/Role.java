package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author AZ
 * @Description 对话角色
 * @creat 2023/12/17 16:02
 */
@Getter
@AllArgsConstructor
public enum Role {
    /**
     * user 用户输入的内容
     */
    user("user"),
    /**
     * 模型生成的内容，model
     */
    model("model"),
    ;
    private final String code;

}