package com.yxw.study.springaidemo.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多模型配置
 */
@Configuration
public class SaaLLMSConfig {
    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    // 多模型共存
    private final String DEEPSEEK_MODEL = "deepseek-v3";

    private final String QWEN_MODEL = "qwen-max";


    /**
     * 多模型指定name
     * @return
     */
    @Bean(name = "deepseekChatModel")
    public ChatModel deepSeek() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(apiKey).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build())
                .build();
    }

    /**
     * 多模型指定name
     * @return
     */
    @Bean(name = "qwenChatModel")
    public ChatModel qwen() {
        return DashScopeChatModel.builder()
                .dashScopeApi(DashScopeApi.builder().apiKey(apiKey).build())
                .defaultOptions(DashScopeChatOptions.builder().withModel(QWEN_MODEL).build())
                .build();
    }



    @Bean(name = "deepseekChatClient")
    public ChatClient deepseekChatClient(@Qualifier("deepseekChatModel") ChatModel deepseekChatModel) {
        return ChatClient.builder(deepseekChatModel)
                .defaultOptions(ChatOptions.builder().model(DEEPSEEK_MODEL).build())
                .build();
    }


    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwenChatModel") ChatModel qwenChatModel) {
        return ChatClient.builder(qwenChatModel)
                .defaultOptions(ChatOptions.builder().model(QWEN_MODEL).build())
                .build();
    }
}
