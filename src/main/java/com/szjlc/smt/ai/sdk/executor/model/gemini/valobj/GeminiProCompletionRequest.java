package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author AZ
 * @Description Gemini PRO请求信息
 * @creat 2023/12/17 13:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeminiProCompletionRequest implements Serializable {

    /**
     * Gemini Pro对话内容
     */
    private List<Content> contents;

    /**
     * 安全设置
     */
    private List<SafetySetting> safetySettings;
    /**
     * 公共配置
     */
    private GenerationConfig generationConfig;



}
