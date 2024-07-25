package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 360智脑请求参数
 *
 * @author xs.wu
 * @className Brain360CompletionRequest
 * @date 2023/12/9 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Brain360CompletionRequest {

    /**
     * 默认模型
     */
    private String model = Model.Brain_360GPT_S2_V9.getCode();

    /**
     * 问题描述
     */
    private List<Message> messages;

    /**
     * 是否为流式输出；就是一蹦一蹦的，出来结果
     */
    private Boolean stream = false;

    /**
     * 取值应⼤于等于 0 ⼩于等于 1，默认值是 0.9，更⾼的值代表结果更随机，较低的值代表结果更聚焦
     */
    private Double temperature;

    /**
     * 输出字符串限制；0 ~ 4096
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens = 2048;

    /**
     * 取值应⼤于等于 0 ⼩于等于 1，默认值是 0.5
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 取值应⼤于等于 0 ⼩于等于 1024，默认值是 0
     */
    @JsonProperty("top_k")
    private Integer topK;

    /**
     * 取值应⼤于等于 1 ⼩于等于 2，默认值是 1.05
     */
    @JsonProperty("repetition_penalty")
    private Double repetitionPenalty;


    /**
     * 取值应⼤于等于 1 ⼩于等于 5，默认值是 1
     */
    @JsonProperty("num_beams")
    private Integer numBeams;

    /**
     * 标记业务⽅⽤户 id，便于业务⽅区分不同⽤户
     */
    private String user;
}
