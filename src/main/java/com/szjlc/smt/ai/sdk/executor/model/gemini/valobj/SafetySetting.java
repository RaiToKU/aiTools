package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * @author AZ
 * @Description 安全设置
 * @creat 2023/12/17 14:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SafetySetting implements Serializable {

    /**
     * 安全类别
     */
    @Builder.Default
    private String category= SafeCategoryType.HARM_CATEGORY_DANGEROUS_CONTENT.getCode();

    /**
     * 安全级别
     */
    @Builder.Default
    private String threshold= SafeThresholdType.BLOCK_ONLY_HIGH.getCode();


    @Getter
    @AllArgsConstructor
    public enum SafeCategoryType {
        /** 针对身份和/或受保护属性的负面或有害评论。 */
        HARM_CATEGORY_HARASSMENT("HARM_CATEGORY_HARASSMENT"),
        /** 针对粗鲁、无礼或亵渎的内容。 */
        HARM_CATEGORY_HATE_SPEECH("HARM_CATEGORY_HATE_SPEECH"),
        /** 针对包含性行为或其他淫秽内容。 */
        HARM_CATEGORY_SEXUALLY_EXPLICIT("HARM_CATEGORY_SEXUALLY_EXPLICIT"),
        /** 针对促进、促进或鼓励有害行为。 */
        HARM_CATEGORY_DANGEROUS_CONTENT("HARM_CATEGORY_DANGEROUS_CONTENT")
        ;

        private final String code;

    }

    @Getter
    @AllArgsConstructor
    public enum SafeThresholdType {
        /** 始终显示（无论是否存在不安全内容的概率） */
        BLOCK_NONE("BLOCK_NONE"),
        /** 在存在高风险的不安全内容时屏蔽 */
        BLOCK_ONLY_HIGH("BLOCK_ONLY_HIGH"),
        /** 出现中等或高概率的不安全内容时屏蔽 */
        BLOCK_MEDIUM_AND_ABOVE("BLOCK_MEDIUM_AND_ABOVE"),
        /** 在存在不安全内容的几率较低、中等或较高时屏蔽 */
        BLOCK_LOW_AND_ABOVE("BLOCK_LOW_AND_ABOVE"),
        /** 未指定阈值，使用默认阈值进行屏蔽 */
        HARM_BLOCK_THRESHOLD_UNSPECIFIED("HARM_BLOCK_THRESHOLD_UNSPECIFIED")
        ;

        private final String code;

    }
}
