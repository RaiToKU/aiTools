package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author AZ
 * @Description Gemini Pro对话内容
 * @creat 2023/12/17 13:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Content implements Serializable {

    /**
     * 用户 user
     * 模型 model
     */
    private String role;
    /**
     * 对话消息列表
     */
    private List<Parts> parts;
}
