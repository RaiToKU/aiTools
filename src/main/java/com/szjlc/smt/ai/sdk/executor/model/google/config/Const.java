package com.szjlc.smt.ai.sdk.executor.model.google.config;

public class Const {
    public static String v1_completions = "v1beta1/";
    public static String v2_completions = "v1beta2/";

    public static String v3_completions = "v1beta3/";

    public static String V1_BATA_GEMINI_PRO_COMPLETIONS = "v1beta/";

    // 聊天模型
    public static String CHAT_MODEL_CODE = "models/chat-bison-001";
    // 文本模型
    public static String TEXT_MODEL_CODE = "models/text-bison-001";
    // 嵌入模型
    public static String EMBED_MODEL_CODE = "embedding-gecko-001";
    // GeminiPro 文本模型
    public static String TEXT_GEMINI_PRO_CODE = "models/gemini-pro:generateContent";
    // GeminiPro 流式 文本模型
    public static String TEXT_GEMINI_PRO_CHAT_CODE = "models/gemini-pro:streamGenerateContent";



}
