package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author AZ
 * @Description 输入消息内容
 * @creat 2023/12/17 13:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parts implements Serializable {

    /**
     * 消息内容
     */
    private String text;
    /**
     * 文本和图像输入，图像相关
     */
    private InlineData inlineData;
}
