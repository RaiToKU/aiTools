package com.szjlc.smt.ai.sdk.executor.model.gemini.valobj;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author AZ
 * @Description 公共配置
 * @creat 2023/12/17 14:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenerationConfig implements Serializable {

    /**
     * 温度
     */
    @Builder.Default
    private Double temperature = 0.5;
    /**
     * 输出令牌数量上限
     */
    @Builder.Default
    private Integer maxOutputTokens = 800;
    /**
     * 多样性控制；使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币
     */
    @Builder.Default
    private Double topP = 0.8;
    /**
     * topK 参数可更改模型选择输出令牌的方式
     */
    @Builder.Default
    private Integer topK = 10;
    /**
     * 停止序列
     */
    @JsonProperty("stop_sequences")
    private List<String> stopSequences;
}
