package com.szjlc.smt.ai.sdk.executor.model.gemini.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AZ
 * @Description Gemini Pro配置信息
 * @creat 2023/12/17 15:17
 */
public class GeminiProConfig {

    @Getter
    @Setter
    private String apiHost = "https://generativelanguage.googleapis.com/";

    @Getter
    @Setter
    private String apiKey;

    @Getter
    private final String v1beta_chat_completions = "v1beta/models/";


}
