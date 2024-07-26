package com.szjlc.smt.ai.test;/**
 * @Author raito
 * @Date 2024/7/25 21:41
 * @PackageName com.szjlc.smt.ai.test
 * @ClassName AliYunTest
 * @Description TODO
 * @Version 1.0
 */

import cn.hutool.json.JSONUtil;
import com.szjlc.smt.ai.sdk.executor.model.aliyun.config.AliModelConfig;
import com.szjlc.smt.ai.sdk.executor.parameter.ChatChoice;
import com.szjlc.smt.ai.sdk.executor.parameter.CompletionRequest;
import com.szjlc.smt.ai.sdk.executor.parameter.CompletionResponse;
import com.szjlc.smt.ai.sdk.executor.parameter.Message;
import com.szjlc.smt.ai.sdk.session.Configuration;
import com.szjlc.smt.ai.sdk.session.DefaultOpenAiSessionFactory;
import com.szjlc.smt.ai.sdk.session.OpenAiSession;
import com.szjlc.smt.ai.sdk.session.OpenAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * @Author raito
 * @Date 2024/7/25 21:41
 * @PackageName com.szjlc.smt.ai.test
 * @ClassName AliYunTest
 * @Description TODO
 * @Version 1.0
 */
@Slf4j
public class AliYunTest {

    private OpenAiSession openAiSession;

    @Test
    public void test_OpenAiSessionFactory() throws Exception {
        // 0. 注意；将 resources 包下的 .env修改为 .env.local 并添加各项配置

        AliModelConfig aliModelConfig = new AliModelConfig();
        aliModelConfig.setApiHost("https://dashscope.aliyuncs.com/compatible-mode/v1");
        aliModelConfig.setApiKey("sk-eaeb39bb3cd248dcac76824fd56ea301");

        // 2. 配置文件
        Configuration configuration = new Configuration();
        configuration.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        configuration.setAliModelConfig(aliModelConfig);

        // 3. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 4. 开启会话
        this.openAiSession = factory.openSession();
        //对话

        // 1. 创建参数
        CompletionRequest request = CompletionRequest.builder()
                .stream(true)
                .messages(Collections.singletonList(Message.builder().role(CompletionRequest.Role.USER).content("1+1").build()))
                .model(CompletionRequest.Model.QWEN_TURBO.getCode())
                .build();
        // 2. 请求等待
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 3. 应答请求
        EventSource eventSource = openAiSession.completions(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id,  String type, String data) {
                if ("[DONE]".equalsIgnoreCase(data)) {
                    log.info("OpenAI 应答完成");
                    return;
                }
                CompletionResponse chatCompletionResponse = JSONUtil.toBean(data, CompletionResponse.class);
                List<ChatChoice> choices = chatCompletionResponse.getChoices();
                for (ChatChoice chatChoice : choices) {
                    Message delta = chatChoice.getDelta();
                    if (CompletionRequest.Role.ASSISTANT.getCode().equals(delta.getRole())) continue;

                    // 应答完成
                    String finishReason = chatChoice.getFinishReason();
                    if (StringUtils.isNoneBlank(finishReason) && "stop".equalsIgnoreCase(finishReason)) {
                        return;
                    }

                    log.info("测试结果：{}", delta.getContent());
                }
            }

            @Override
            public void onClosed(EventSource eventSource) {
                log.info("对话完成");
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                log.info("对话异常");
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
    }


}
