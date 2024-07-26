package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author AZ
 * @Description 安全性评分
 * @creat 2023/12/17 15:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SafetyRating implements Serializable {

    /**
     * 安全类别
     */
    private String category;

    /**
     * 类别安全评分
     */
    private String probability;
}
