package com.szjlc.smt.ai.sdk.executor.model.baidu.valobj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文心一言返回信息
 */
@Data
public class BaiduCompletionResponse implements Serializable {

    /**
     * 本轮对话的id
     */
    private String id;

    /**
     * 回包类型 chat.completion：多轮对话返回
     */
    private String object;

    /**
     * 时间戳
     */
    private Long created;

    /**
     * 表示当前子句的序号。只有在流式接口模式下会返回该字段
     */
    @JsonProperty("sentence_id")
    private Integer sentenceId;

    /**
     * 表示当前子句是否是最后一句。只有在流式接口模式下会返回该字段
     */
    @JsonProperty("is_end")
    private Boolean isEnd;

    /**
     * 当前生成的结果是否被截断
     */
    @JsonProperty("is_truncated")
    private Boolean isTruncated;

    /**
     * 输出内容标识
     */
    @JsonProperty("finish_reason")
    private String finishReason;

    /**
     * 搜索数据，当请求参数enable_citation为true并且触发搜索时，会返回该字段
     */
    @JsonProperty("search_info")
    private String searchInfo;

    /**
     * 当前生成的结果是否被截断
     */
    private String result;

    /**
     * 表示用户输入是否存在安全，是否关闭当前会话，清理历史会话信息
     */
    @JsonProperty("need_clear_history")
    private Boolean needClearHistory;

    /**
     * 第几轮对话有敏感信息，如果是当前问题，ban_round=-1
     */
    @JsonProperty("ban_round")
    private Integer banRound;

    /**
     * token统计信息，token数 = 汉字数+单词数*1.3
     */
    private Usage usage;
}
