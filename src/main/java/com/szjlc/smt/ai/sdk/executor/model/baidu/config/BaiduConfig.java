package com.szjlc.smt.ai.sdk.executor.model.baidu.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class BaiduConfig {

    @Getter
    @Setter
    private String apiHost = "https://aip.baidubce.com/rpc/2.0/ai_custom/";

    /**
     * token 授权地址
     */
    @Getter
    @Setter
    private String authHost = "https://aip.baidubce.com/oauth/2.0/token";

    @Getter
    @Setter
    private String apiKey;

    @Getter
    @Setter
    private String apiSecret;

    /**
     * 通过 apiKey 和 apiSecret 生成 token
     */
    @Getter
    @Setter
    private String accessToken;

    @Getter
    @AllArgsConstructor
    public enum CompletionsUrl {
        ERNIE_Bot_turbo("ERNIE_Bot_turbo", "v1/wenxinworkshop/chat/eb-instant"),
        ERNIE_Bot("ERNIE_Bot", "v1/wenxinworkshop/chat/completions"),
        ERNIE_Bot_4("ERNIE_Bot_4", "v1/wenxinworkshop/chat/completions_pro"),
        ERNIE_Bot_8K("ERNIE_Bot_8K", "v1/wenxinworkshop/chat/ernie_bot_8k"),
        Stable_Diffusion_XL("Stable_Diffusion_XL", "v1/wenxinworkshop/text2image/sd_xl"),
        ;
        private final String code;
        private final String url;
    }
}
