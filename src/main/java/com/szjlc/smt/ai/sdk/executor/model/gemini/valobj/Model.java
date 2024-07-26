package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author AZ
 * @Description Gemini Pro模型
 * @creat 2023/12/17 15:53
 */
@Getter
@AllArgsConstructor
public enum Model {

    GEMINI_PRO("gemini_pro","gemini-pro:streamGenerateContent"),

    GEMINI_PRO_VERSION("gemini_pro_version","gemini-pro-vision:generateContent")
    ;

    private final String code;
    private final String path;
}
