package com.szjlc.smt.ai.sdk.session;

import com.szjlc.smt.ai.sdk.executor.Executor;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.AliModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.config.AliModelConfig;
import com.szjlc.smt.ai.sdk.executor.model.baidu.BaiduModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.baidu.config.BaiduConfig;
import com.szjlc.smt.ai.sdk.executor.model.brain360.Brain360ModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.brain360.config.Brain360Config;
import com.szjlc.smt.ai.sdk.executor.model.chatglm.CharGLMModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.chatglm.ChatGLMModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.chatglm.config.ChatGLMConfig;
import com.szjlc.smt.ai.sdk.executor.model.chatgpt.ChatGPTModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.chatgpt.config.ChatGPTConfig;
import com.szjlc.smt.ai.sdk.executor.model.claude.config.ClaudeConfig;
import com.szjlc.smt.ai.sdk.executor.model.gemini.GeminiProModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.gemini.config.GeminiProConfig;
import com.szjlc.smt.ai.sdk.executor.model.google.PalmChatModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.google.PalmTextModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.google.config.PalmConfig;
import com.szjlc.smt.ai.sdk.executor.model.tencent.TencentModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.tencent.config.TencentConfig;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.XunFeiModelExecutor;
import com.szjlc.smt.ai.sdk.executor.model.xunfei.config.XunFeiConfig;
import com.szjlc.smt.ai.sdk.executor.parameter.CompletionRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;

import java.util.HashMap;

/**
 * 配置文件
 *
 * @author 小傅哥，微信：fustack
 */
@Slf4j
@Data
public class Configuration {

    /**
     * 智谱Ai ChatGLM Config
     */
    private ChatGLMConfig chatGLMConfig;

    /**
     * OpenAi ChatGLM Config
     */
    private ChatGPTConfig chatGPTConfig;

    /**
     * 讯飞
     */
    private XunFeiConfig xunFeiConfig;

    /**
     * 阿里通义千问
     */
    private AliModelConfig aliModelConfig;

    /**
     * 百度文心一言
     */
    private BaiduConfig baiduConfig;

    /**
     * 腾讯混元
     */
    private TencentConfig tencentConfig;

    /**
     * Google Palm2
     */
    private PalmConfig palmConfig;

    /**
     * 360智脑
     */
    private Brain360Config brain360Config;

    /**
     * claudeConfig
     */
    private ClaudeConfig claudeConfig;

    /**
     * GeminiProConfig
     */
    private GeminiProConfig geminiProConfig ;

    /**
     * OkHttpClient
     */
    private OkHttpClient okHttpClient;

    private HashMap<String, Executor> executorGroup;

    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }

    // OkHttp 配置信息
    private HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.HEADERS;
    private long connectTimeout = 4500;
    private long writeTimeout = 4500;
    private long readTimeout = 4500;

    // http keywords
    public static final String SSE_CONTENT_TYPE = "text/event-stream";
    public static final String DEFAULT_USER_AGENT = "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)";
    public static final String APPLICATION_JSON = "application/json";
    public static final String JSON_CONTENT_TYPE = APPLICATION_JSON + "; charset=utf-8";

    public HashMap<String, Executor> newExecutorGroup() {
        this.executorGroup = new HashMap<>();
        // ChatGLM 类型执行器填充
        Executor chatGLMModelExecutor = new ChatGLMModelExecutor(this);
        Executor charGLMModelExecutor = new CharGLMModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.CHATGLM_TURBO.getCode(), chatGLMModelExecutor);
        executorGroup.put(CompletionRequest.Model.CHARGLM_3.getCode(), charGLMModelExecutor);
        // ChatGPT 类型执行器填充
        Executor chatGPTModelExecutor = new ChatGPTModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.GPT_3_5_TURBO.getCode(), chatGPTModelExecutor);
        executorGroup.put(CompletionRequest.Model.GPT_3_5_TURBO_1106.getCode(), chatGPTModelExecutor);
        executorGroup.put(CompletionRequest.Model.GPT_3_5_TURBO_16K.getCode(), chatGPTModelExecutor);
        executorGroup.put(CompletionRequest.Model.GPT_4.getCode(), chatGPTModelExecutor);
        executorGroup.put(CompletionRequest.Model.GPT_4_32K.getCode(), chatGPTModelExecutor);
        executorGroup.put(CompletionRequest.Model.DALL_E_2.getCode(), chatGPTModelExecutor);
        executorGroup.put(CompletionRequest.Model.DALL_E_3.getCode(), chatGPTModelExecutor);
        // XUNFEI
        Executor xunfeiModelExecutor = new XunFeiModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.XUNFEI.getCode(), xunfeiModelExecutor);
        // 阿里通义千问
        Executor aliModelExecutor = new AliModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.QWEN_TURBO.getCode(), aliModelExecutor);
        executorGroup.put(CompletionRequest.Model.QWEN_PLUS.getCode(), aliModelExecutor);
        executorGroup.put(CompletionRequest.Model.QWEN_MAX.getCode(), aliModelExecutor);
        // 百度文心一言
        Executor wenXinModelExecutor = new BaiduModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.ERNIE_BOT_TURBO.getCode(), wenXinModelExecutor);
        executorGroup.put(CompletionRequest.Model.ERNIE_BOT.getCode(), wenXinModelExecutor);
        executorGroup.put(CompletionRequest.Model.ERNIE_Bot_4.getCode(), wenXinModelExecutor);
        executorGroup.put(CompletionRequest.Model.ERNIE_Bot_8K.getCode(), wenXinModelExecutor);
        executorGroup.put(CompletionRequest.Model.STABLE_DIFFUSION_XL.getCode(), wenXinModelExecutor);
        // 腾讯混元
        Executor tencentExecutor = new TencentModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.HUNYUAN_CHATSTD.getCode(), tencentExecutor);
        executorGroup.put(CompletionRequest.Model.HUNYUAN_CHATPRO.getCode(), tencentExecutor);

        // 360智脑
        Executor brain360Executor = new Brain360ModelExecutor(this);
        executorGroup.put(CompletionRequest.Model.Brain_360GPT_S2_V9.getCode(), brain360Executor);

        // Google Palm
        executorGroup.put(CompletionRequest.Model.PALM_CHAT.getCode(), new PalmChatModelExecutor(this));
        executorGroup.put(CompletionRequest.Model.PALM_TEXT.getCode(), new PalmTextModelExecutor(this));


        return this.executorGroup;
    }

}
