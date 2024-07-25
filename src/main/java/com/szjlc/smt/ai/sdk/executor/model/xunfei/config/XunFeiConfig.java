package com.szjlc.smt.ai.sdk.executor.model.xunfei.config;

import lombok.Data;

/**
 * 讯飞 配置信息
 *
 * @author 小傅哥，微信：fustack
 */
@Data
public class XunFeiConfig {

    private String apiHost = "https://spark-api.xf-yun.com/v3.1/chat";
    private String apiTtiHost = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/tti";
    private String apiPictureHost = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/image";
    private String appid;
    private String apiKey;
    private String apiSecret;

}
