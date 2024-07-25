package com.szjlc.smt.ai.sdk.executor.model.google.valobj.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class MessagePrompt {
    /**
     * 非必需，提供给模型以支撑响应的文本
     */
    private String context;
    /**
     * 非必需，模型应生成的示例
     */
    private List<PalmMessage> examples;
    /**
     * 必须，按时间顺序排序的最近对话历史记录的快照
     */
    private List<PalmMessage> messages;

}
