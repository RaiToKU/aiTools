package com.szjlc.smt.ai.sdk.executor.model.chatglm.valobj;

import lombok.Data;

/**
 * ChatGLM 应答参数
 *
 * @author 小傅哥，微信：fustack
 */
@Data
public class ChatGLMCompletionResponse {

    private String data;
    private String meta;

    @Data
    public static class Meta {
        private String task_status;
        private Usage usage;
        private String task_id;
        private String request_id;
    }

    @Data
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }

}
