package com.szjlc.smt.ai.sdk.executor.model.google.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * GeminiPro 配置信息
 *
 * @author 小傅哥，微信：fustack, fy
 */
@Getter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class GeminiProConfig {

    // GeminiPro 请求地址
    @Setter
    private String apiHost = "https://generativelanguage.googleapis.com/";

    @Setter
    @Getter
    private String apiKey = "";

}
