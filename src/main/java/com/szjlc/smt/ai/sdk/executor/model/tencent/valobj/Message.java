package com.szjlc.smt.ai.sdk.executor.model.tencent.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private static final long serialVersionUID = 1L;
    /**
     * 角色
     */
    @JsonProperty("Role")
    private String role;
    /**
     * 消息的内容
     */
    @JsonProperty("Content")
    private String content;


    public static Message of(com.szjlc.smt.ai.sdk.executor.parameter.Message source) {
        Message target = new Message();
        target.setRole(source.getRole());
        target.setContent(source.getContent());
        return target;
    }
}
