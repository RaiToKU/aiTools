package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对某个特定响应的一部分的来源的引用
 */

@Data
@NoArgsConstructor
public class CitationSource {
    private Integer startIndex;
    private Integer endIndex;
    private String uri;
    private String license;
}
