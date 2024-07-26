package com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PictureText {

    /**
     * 取值为[user,assistant]
     */
    private String role;

    /**
     * 所有content的累计tokens需控制8192以内
     */
    private String content;

    /**
     * 到底是图片还是问题描述
     */
    @JsonProperty("content_type")
    private String contentType;

    @Getter
    public enum Role {

        USER("user"),
        ASSISTANT("assistant"),
        ;

        Role(String name) {
            this.name = name;
        }

        private final String name;
    }

}
