package com.szjlc.smt.ai.sdk.executor.model.xunfei.valobj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PictureChat {
    /**
     * 取值为[general,generalv2]
     * 指定访问的领域,general指向V1.5版本 generalv2指向V2版本。注意：不同的取值对应的url也不一样！
     */
    private String domain;
    /**
     * 取值为[0,1],默认为0.5
     */
    @Builder.Default
    private double temperature = 0.5;
    /**
     * 取值为[1,4096]，默认为2048
     */
    @Builder.Default
    @JsonProperty("max_tokens")
    private Integer maxTokens = 2048;
    /**
     * 取值为[1，6],默认为4
     * 从k个候选中随机选择⼀个（⾮等概率）
     */
    @Builder.Default
    @JsonProperty("top_k")
    private Integer topK = 1;

    @Builder.Default
    private String audting = "default";
}
