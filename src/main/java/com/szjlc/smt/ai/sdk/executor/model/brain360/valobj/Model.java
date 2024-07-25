package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;

import lombok.AllArgsConstructor;

/**
 * 模型
 *
 * @author xs.wu
 * @className Model
 * @date 2023/12/9 12:02
 */
@AllArgsConstructor
public enum Model {

    Brain_360GPT_S2_V9("360GPT_S2_V9", "360"),
    ;

    private final String code;
    private final String info;

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
