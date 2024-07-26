package com.szjlc.smt.ai.sdk.executor.model.google.valobj.response;

import com.szjlc.smt.ai.sdk.executor.model.google.valobj.request.PalmMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PalmCompletionResponse {
    /**
     * 候选消息
     */
    private List<Candidate> candidates;
    /**
     * 历史消息
     */
    private List<PalmMessage> messages;

}
