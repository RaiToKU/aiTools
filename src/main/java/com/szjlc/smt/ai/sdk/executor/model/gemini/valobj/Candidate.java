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
 * @Description 模型的候选响应
 * @creat 2023/12/17 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Candidate implements Serializable {

    /**
     * 内容信息
     */
    private Content content;

    /**
     * 结束原因
     */
    private String finishReason;

    /**
     * 下标
     */
    private Integer index;

    /**
     * 安全性评分列表
     */
    private List<SafetyRating> safetyRatings;

    /**
     * 消耗token数，有时候不返回
     */
    private Integer tokenCount;
}
