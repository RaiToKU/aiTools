package com.szjlc.smt.ai.sdk.executor.model.google.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("0"),
    SYSTEM("1");
    private String code;
}
