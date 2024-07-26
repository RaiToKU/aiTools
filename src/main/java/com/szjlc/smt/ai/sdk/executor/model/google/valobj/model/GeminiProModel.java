package com.szjlc.smt.ai.sdk.executor.model.google.valobj.model;

import lombok.Getter;

import static com.szjlc.smt.ai.sdk.executor.model.google.config.Const.TEXT_GEMINI_PRO_CHAT_CODE;
import static com.szjlc.smt.ai.sdk.executor.model.google.config.Const.TEXT_GEMINI_PRO_CODE;

public enum GeminiProModel {

    TEXT(TEXT_GEMINI_PRO_CODE, initTextModel()),
    TEXT_STREAM(TEXT_GEMINI_PRO_CHAT_CODE, initTextStreamModel());

    @Getter
    private final String code;
    @Getter
    private final Model model;

    GeminiProModel(String code, Model model) {
        this.code = code;
        this.model = model;
    }

    public static Model getModel(String code) {
        for (GeminiProModel value : values()) {
            if (value.getCode().equals(code)) {
                return value.getModel();
            }
        }
        return null;
    }

    private static Model initTextModel() {
        return Model.builder().name("models/gemini-pro").displayName("Gemini Pro")
                .version("001")
                .description("The best model for scaling across a wide range of tasks")
                .supportMethod(SupportMethod.builder().generateMethod("generateContent").build()).build();
    }

    private static Model initTextStreamModel() {
        return Model.builder().name("models/gemini-pro").displayName("Gemini Pro")
                .version("001")
                .description("The best model for scaling across a wide range of tasks")
                .supportMethod(SupportMethod.builder().generateMethod("streamGenerateContent").build()).build();
    }

}
