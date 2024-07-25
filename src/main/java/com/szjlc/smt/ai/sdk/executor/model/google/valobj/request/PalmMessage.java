package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息
 */
@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PalmMessage {
    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private String content;
    /**
     * content在此生成的模型的引用信息Message
     */
    private CitationMetadata citationMetadata;
}