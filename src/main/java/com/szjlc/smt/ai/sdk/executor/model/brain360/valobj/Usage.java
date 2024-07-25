package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 使用量
 *
 * @author xs.wu
 * @className Usage
 * @date 2023/12/9 12:02
 */
@Data
public class Usage implements Serializable {

    /**
     * 问题 tokens 数
     */
    @JsonProperty("prompt_tokens")
    private long promptTokens;
    /**
     * 回答 tokens 数
     */
    @JsonProperty("completion_tokens")
    private long completionTokens;
    /**
     * tokens 总数
     */
    @JsonProperty("total_tokens")
    private long totalTokens;
}
