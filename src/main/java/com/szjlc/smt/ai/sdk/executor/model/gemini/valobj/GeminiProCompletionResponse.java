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
 * @Description Gemini Pro 返回结果
 * @creat 2023/12/17 15:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiProCompletionResponse implements Serializable {

    /**
     * 模型的候选响应
     */
    private List<Candidate> candidates;

    /**
     * 与内容过滤器相关的提示反馈
     */
    private PromptFeedBack promptFeedBack;
}
