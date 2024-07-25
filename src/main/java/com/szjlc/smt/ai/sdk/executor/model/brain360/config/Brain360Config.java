package com.szjlc.smt.ai.sdk.executor.model.brain360.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * 360智脑配置类
 *
 * @author xs.wu
 * @className Brain360Config
 * @date 2023/12/9 11:55
 */
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Brain360Config {

    // 360智脑 请求地址
    @Getter
    @Setter
    private String apiHost = "https://api.360.cn/";

    @Getter
    @Setter
    private String apiKey;

    @Getter
    private String v1_chat_completions = "v1/chat/completions";

}
