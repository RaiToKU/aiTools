package com.szjlc.smt.ai.sdk.executor.model.aliyun.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通义千问 对话消息
 *
 * @author Vanffer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 消息的角色
     */
    private String role;

    /**
     * 对话内容
     */
    private String content;
}
