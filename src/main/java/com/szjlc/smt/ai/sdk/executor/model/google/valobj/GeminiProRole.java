package com.szjlc.smt.ai.sdk.executor.model.google.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeminiProRole {
    USER("user"),
    SYSTEM("model");
    private String code;
}
