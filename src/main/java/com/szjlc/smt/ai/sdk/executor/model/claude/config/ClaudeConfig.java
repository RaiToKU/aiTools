package com.szjlc.smt.ai.sdk.executor.model.claude.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Claude 配置信息
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ClaudeConfig {

    // Claude ChatGlM 请求地址
    @Setter
    private String apiHost = "";

    @Getter
    private String v3_completions = "";

    // Claude
    private String apiSecretKey;


    @Getter
    private String apiKey;
    @Getter
    private String apiSecret;

}
