package com.yxw.study.springaidemo.config;


import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {


    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    /**
     * 通过yaml文件读取
     *
     * @return
     */
    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder().apiKey(apiKey).build();
    }


    /**
     * 通过环境变量获取
     */
 /*   @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder().apiKey(System.getenv("api-key")).build();
    }*/


    @Bean("chatClient")
    public ChatClient chatClient(@Qualifier("qwenChatModel") ChatModel qwenChatModel) {
       return ChatClient.builder(qwenChatModel).build();
    }
}
