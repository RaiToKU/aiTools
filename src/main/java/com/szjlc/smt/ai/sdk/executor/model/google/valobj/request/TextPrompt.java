package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rsy
 * @date 2023/12/10 12:58
 * @description 文本提示模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextPrompt {

    private String text;
}
