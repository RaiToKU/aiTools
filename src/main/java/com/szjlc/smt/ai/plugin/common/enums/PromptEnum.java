package com.szjlc.smt.ai.plugin.common.enums;

/**
 * @Author raito
 * @Date 2024/7/17 9:49
 * @PackageName com.szjlc.smt.ai.plugin.common.enums
 * @ClassName PromptEnum
 * @Description Prompt
 * @Version 1.0
 */
public enum PromptEnum {

    /**
     * prompt
     */

    ;


    private final String desc;

    private final String path;

    PromptEnum(String desc, String path) {
        this.desc = desc;
        this.path = path;
    }

    public String getDesc() {
        return desc;
    }

    public String getPath() {
        return path;
    }
}
