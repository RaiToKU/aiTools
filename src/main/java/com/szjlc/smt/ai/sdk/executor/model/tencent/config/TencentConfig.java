package com.szjlc.smt.ai.sdk.executor.model.tencent.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TencentConfig {
    /**
     * ApiHost
     */
    @Builder.Default
    private String apiHost = "https://hunyuan.tencentcloudapi.com";
    /**
     * SecretId
     */
    private String secretId;
    /**
     * SecretKey
     */
    private String secretKey;
    /**
     * 地域
     */
    @Builder.Default
    private String region = "ap-beijing";
    /**
     * ApiVersion
     */
    @Builder.Default
    private String apiVersion = "2023-09-01";
}
