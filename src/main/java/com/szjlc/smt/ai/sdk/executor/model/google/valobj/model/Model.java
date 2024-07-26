package com.szjlc.smt.ai.sdk.executor.model.google.valobj.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Model {
    private String name;
    private String version;
    private String displayName;
    private String description;
    private Integer inputTokenLimit;
    private Integer outputTokenLimit;
    private List<String> supportedGenerationMethods;
    private SupportMethod supportMethod;
    private Double temperature;
    private Double topP;
    private Integer topK;
}
