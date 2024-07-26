package com.szjlc.smt.ai.sdk.executor.model.baidu.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    ERNIE_Bot_turbo("ERNIE-Bot-turbo", "百度自行研发的大语言模型，覆盖海量中文数据，具有更强的对话问答、内容创作生成等能力，响应速度更快。"),
    ERNIE_Bot("ERNIE-Bot", "百度自行研发的大语言模型，覆盖海量中文数据，具有更强的对话问答、内容创作生成等能力。"),
    ERNIE_Bot_4("ERNIE-Bot-4", "百度自行研发的大语言模型，覆盖海量中文数据，具有更强的对话问答、内容创作生成等能力。"),
    ERNIE_Bot_8K("ERNIE-Bot-8K", "百度⾃⾏研发的⼤语⾔模型，覆盖海量中⽂数据，具有更强的对话问答、内容创作⽣成等能⼒，支持7K输入+1K输出。"),
    Stable_Diffusion_XL("Stable-Diffusion-XL", "业内知名的跨模态大模型，由StabilityAI研发并开源，有着业内领先的图像生成能力"),
    ;
    private final String code;
    private final String info;
}
