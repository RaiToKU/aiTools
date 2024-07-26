package com.szjlc.smt.ai.sdk.executor.model.aliyun.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 通义千问配置信息
 *
 * @author Vanffer
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class AliModelConfig {

    @Getter
    @Setter
    private String apiHost = "https://dashscope.aliyuncs.com/";

    @Getter
    private String v1_completions = "api/v1/services/aigc/text-generation/generation";

    @Getter
    @Setter
    private String apiKey;

}
