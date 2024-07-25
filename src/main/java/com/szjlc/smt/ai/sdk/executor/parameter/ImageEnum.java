package com.szjlc.smt.ai.sdk.executor.parameter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 图片枚举配置
 *
 * @author 小傅哥，微信：fustack
 */
public class ImageEnum {

    @Getter
    @AllArgsConstructor
    public enum Size {
        size_256("256x256"),
        size_512("512x512"),
        size_1024("1024x1024"),
        ;
        private String code;
    }

    @Getter
    @AllArgsConstructor
    public enum ResponseFormat {
        URL("url"),
        B64_JSON("b64_json"),
        ;
        private String code;
    }

}
