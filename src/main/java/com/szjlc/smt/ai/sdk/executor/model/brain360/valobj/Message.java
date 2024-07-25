package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话记录信息
 *
 * @author xs.wu
 * @className Message
 * @date 2023/12/9 12:00
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private String role;
    private String content;
}
