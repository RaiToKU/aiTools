package com.szjlc.smt.ai.sdk.executor.model.baidu.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {

    /**
     * 角色
     * user: 表示用户
     * assistant: 表示对话助手
     */
    private String role;

    /**
     * 对话内容
     */
    private String content;
}
