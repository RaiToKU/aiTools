package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author AZ
 * @Description 消息图像内容
 * @creat 2023/12/17 13:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InlineData implements Serializable {

    /**
     * 图像类型
     */
    @Builder.Default
    private String mimeType="image/jpeg";

    /**
     * 经过Base64编码的图像数据
     */
    @JsonProperty("data")
    private String imgData;

}
