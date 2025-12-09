package com.yxw.study.springaidemo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatClient")
public class ChatClientController {


    // 不持支自动注入 依赖chatModel
    /*    private final ChatClient chatClient;

    public ChatClientController(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }*/

    // 启动时注入
    @Resource(name = "chatClient")
    private ChatClient chatClient;

    @GetMapping(value = "/doChat/call")
    public String doChatCall(@RequestParam String msg) {
        return chatClient
                .prompt()
                .user(msg)
                .call()
                .content();

    }
}
