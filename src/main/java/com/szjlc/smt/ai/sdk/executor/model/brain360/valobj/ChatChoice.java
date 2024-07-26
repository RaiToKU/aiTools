package com.szjlc.smt.ai.sdk.executor.model.brain360.valobj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 对话信息
 *
 * @author xs.wu
 * @className ChatChoice
 * @date 2023/12/9 11:56
 */
@Data
public class ChatChoice implements Serializable {

    private long index;
    /**
     * stream = true 请求参数里返回的属性是 delta
     */
    @JsonProperty("delta")
    private Message delta;
    /**
     * stream = false 请求参数里返回的属性是 delta
     */
    @JsonProperty("message")
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;

}
