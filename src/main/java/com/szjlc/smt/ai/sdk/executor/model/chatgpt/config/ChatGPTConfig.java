package com.szjlc.smt.ai.sdk.executor.model.chatgpt.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * OpenAi 配置信息
 *
 * @author 小傅哥，微信：fustack
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTConfig {

    @Getter
    @Setter
    private String apiHost = "https://api.openai.com/";

    @Getter
    @Setter
    private String apiKey;

    @Getter
    private String v1_chat_completions = "v1/chat/completions";
    @Getter
    String v1_images_completions = "v1/images/generations";
    @Getter
    String v1_images_edits = "v1/images/edits";

}
