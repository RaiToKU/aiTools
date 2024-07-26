package com.szjlc.smt.ai.sdk.executor.model.baidu.valobj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaiduCompletionRequest {

    /**
     * 问题描述
     */
    private List<Message> messages;

    /**
     * 是否为流式输出；就是一蹦一蹦的，出来结果
     */
    private Boolean stream = false;

    /**
     * 默认0.95，范围 (0, 1.0]，不能为 0 , 较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定
     */
    private Double temperature;

    /**
     * 多影响输出文本的多样性，取值越大，生成文本的多样性越强, 默认0.7，取值范围 [0, 1.0]
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 通过对已生成的token增加惩罚，减少重复生成的现象, 默认1.0，取值范围：[1.0, 2.0], 值越大表示惩罚越大
     */
    @JsonProperty("penalty_score")
    @Builder.Default
    private Double penaltyScore = 1.0;

    /**
     * 模型人设，主要用于人设设定，长度限制1024个字符
     */
    private String system;

    /**
     * 生成停止标识，当模型生成结果以stop中某个元素结尾时，停止文本生成。说明：
     * （1）每个元素长度不超过20字符
     * （2）最多4个元素
     */
    private List<String> stop;

    /**
     * 是否强制关闭实时搜索功能，默认false，表示不关闭
     */
    @JsonProperty("disable_search")
    private Boolean disableSearch;

    /**
     * 是否开启上角标返回，说明：
     * （1）开启后，有概率触发搜索溯源信息search_info，search_info内容见响应参数介绍
     * （2）默认false，不开启
     */
    @JsonProperty("enable_citation")
    private Boolean enableCitation;

    /**
     * 表示最终用户的唯一标识符，可以监视和检测滥用行为，防止接口恶意调用
     */
    @JsonProperty("user_id")
    private String userId;
}
