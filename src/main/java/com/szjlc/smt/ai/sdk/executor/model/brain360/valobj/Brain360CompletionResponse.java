package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 360智脑 对话响应结果信息
 *
 * @author xs.wu
 * @className Brain360CompletionResponse
 * @date 2023/12/9 11:18
 */
@Data
public class Brain360CompletionResponse implements Serializable {

    /**
     * ID
     */
    private String id;
    /**
     * 对象
     */
    private String object;
    /**
     * 模型
     */
    private String model;
    /**
     * 对话
     */
    private List<ChatChoice> choices;
    /**
     * 创建
     */
    private long created;
    /**
     * 非流式才会响应，流式无法响应
     * 耗材
     */
    private Usage usage;

}
