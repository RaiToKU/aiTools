package com.szjlc.smt.ai.sdk.executor.model.google.valobj.model;

import lombok.Getter;

import static com.szjlc.smt.ai.sdk.executor.model.google.config.Const.CHAT_MODEL_CODE;
import static com.szjlc.smt.ai.sdk.executor.model.google.config.Const.TEXT_MODEL_CODE;


public enum PalmModel {

    TEXT(TEXT_MODEL_CODE, initTextModel()),
    CHAT(CHAT_MODEL_CODE, initChatModel());

    @Getter
    private final String code;
    @Getter
    private final Model model;

    PalmModel(String code, Model model) {
        this.code = code;
        this.model = model;
    }


    public static Model getModel(String code) {
        for (PalmModel value : values()) {
            if (value.getCode().equals(code)) {
                return value.getModel();
            }
        }
        return null;
    }

    private static Model initTextModel() {
        return Model.builder().name(TEXT_MODEL_CODE).displayName("Text Bison")
                .version("001")
                .description("Model targeted for text generation.")
                .inputTokenLimit(8196).outputTokenLimit(1024)
                .supportMethod(SupportMethod.builder().generateMethod("generateText").build()).build();
    }


    private static Model initChatModel() {
        return Model.builder().name(CHAT_MODEL_CODE).displayName("Chat Bison")
                .version("001")
                .description("Chat-optimized generative language model.")
                .inputTokenLimit(4096).outputTokenLimit(1024)
                .supportMethod(new SupportMethod("generateMessage", "countMessageTokens")).build();
    }
}
