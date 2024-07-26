// Aa.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
/**
 * GeminiPro 请求参数
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Slf4j
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiProTextRequest {

    private List<SafetySetting> safetySettings;
    private List<Content> contents;
    private GenerationConfig generationConfig;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Content {
        private String role;
        private List<TextPrompt> parts;
    }

    public void setMessage(String message, String user) {
        TextPrompt textPrompt = new TextPrompt();
        textPrompt.setText(message);
        Content content = Content.builder()
                .role(user)
                .parts(Lists.newArrayList(textPrompt))
                .build();
        contents = Lists.newArrayList(content);
    }

    @Data
    public static class GenerationConfig {

        private List<String> stopSequences;
        private Double temperature = 1.0;
        private Integer maxOutputTokens = 800;
        private Double topP = 0.8;
        private Integer topK = 10;

    }

    @Data
    public static class SafetySetting {
        private String threshold;
        private String category;
    }
}
