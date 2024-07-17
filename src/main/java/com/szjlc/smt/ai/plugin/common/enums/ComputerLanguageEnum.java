package com.szjlc.smt.ai.plugin.common.enums;

import com.szjlc.smt.ai.plugin.common.constant.CommonConstant;

/**
 * @Author raito
 * @Date 2024/7/17 9:49
 * @PackageName com.szjlc.smt.ai.plugin.common.enums
 * @ClassName ComputerLanguageEnum
 * @Description Computer Language
 * @Version 1.0
 */
public enum ComputerLanguageEnum {
    /**
     * computer language
     */
    JAVA,

    ANDROID,

    KOTLIN,

    SWIFT,

    OC,

    C,

    CPP,

    RUBY,

    GO,

    PYTHON,

    PHP,

    TYPE_SCRIPT,

    VUE2,

    VUE3,

    REACT,

    FLUTTER,

    ANGULAR,

    /**
     * 自定义
     */
    NONE,
    ;


    public static String getLowercaseName(ComputerLanguageEnum computerLanguageEnum) {
        String name = computerLanguageEnum.name().toLowerCase();
        return name.replaceAll(CommonConstant.UNDERLINE, CommonConstant.EMPTY);
    }

}
