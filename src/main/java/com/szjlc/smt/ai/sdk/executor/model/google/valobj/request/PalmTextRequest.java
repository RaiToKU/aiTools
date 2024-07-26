package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PalmTextRequest {
    private TextPrompt prompt;
    private Double temperature = 0.1;
    @JsonProperty("candidate_count")
    private Integer candidateCount = 1;
    private Double topP = 0.8;
    private Integer topK = 10;
}
