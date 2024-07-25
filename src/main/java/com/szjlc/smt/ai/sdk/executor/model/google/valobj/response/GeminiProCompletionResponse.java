// Aa.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.szjlc.smt.ai.sdk.executor.model.google.valobj.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * GeminiPro 应答参数
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiProCompletionResponse {


    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Candidate {
        private String finishReason;
        private long index;
        private List<SafetyRating> safetyRatings;
        private Content content;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {
        private String role;
        private List<Part> parts;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Part {
        private String text;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SafetyRating {
        private String probability;
        private String category;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromptFeedback {
        private List<SafetyRating> safetyRatings;
    }

}