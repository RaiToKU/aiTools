package com.szjlc.smt.ai.sdk.executor.model.tencent.valobj;

import com.szjlc.smt.ai.sdk.executor.parameter.CompletionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Action {
    CHAT_STD("ChatStd", "适用于对知识量、推理能力、创造力要求较高的场景"),
    CHAT_PRO("ChatPro", "适用于对知识量、推理能力、创造力要求较高的场景");
    private final String code;
    private final String info;

    public static String of(String model) {
        if (CompletionRequest.Model.HUNYUAN_CHATSTD.getCode().equals(model)) {
            return CHAT_STD.getCode();
        } else if (CompletionRequest.Model.HUNYUAN_CHATPRO.getCode().equals(model)) {
            return CHAT_PRO.getCode();
        } else {
            throw new IllegalArgumentException("未知的模型名称");
        }
    }
}
