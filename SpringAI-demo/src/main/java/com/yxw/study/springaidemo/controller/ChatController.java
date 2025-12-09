package com.yxw.study.springaidemo.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {


    @Resource(name = "deepseekChatModel")
    private ChatModel chatModel;


    @GetMapping(value = "/doChat/call")
    public String doChatCall(@RequestParam String msg) {
        return chatModel.call(msg);

    }

    @GetMapping(value = "/doChat/flux")
    public Flux<String> doChatFlux(@RequestParam String msg) {
        return chatModel.stream(msg);
    }





}
