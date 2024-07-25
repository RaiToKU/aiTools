package com.szjlc.smt.ai.sdk.executor.model.baidu.valobj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ctt
 * @date 2023年12月15日 星期五 15:41
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaiduImageRequest {
    /**
     * 提示词
     */
    private String prompt;
    /**
     * 反向提示词
     */
    private String negativePrompt;
    /**
     * 图片尺寸
     */
    private String size;
    /**
     * 生成图片数量
     */
    private int n;
    /**
     * 迭代轮次
     */
    private int steps;
    /**
     * 采样方式
     */
    private String samplerIndex;
    /**
     * 用户标识符（可选）
     */
    private String userId;
}