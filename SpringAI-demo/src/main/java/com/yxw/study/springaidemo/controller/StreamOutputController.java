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
@RequestMapping("/stream")
public class StreamOutputController {


    // 通过chatModel实现stream 实现流式输出
    @Resource(name = "deepseekChatModel")
    private ChatModel deepseekChatModel;

    @Resource(name = "qwenChatModel")
    private ChatModel qwenChatModel;



    @GetMapping(value = "/doChat/call")
    public String doChatCall(@RequestParam String msg) {
        return deepseekChatModel.call(msg);

    }

    @GetMapping(value = "/doChat/flux")
    public Flux<String> doChatFlux(@RequestParam String msg) {
        return qwenChatModel.stream(msg);
    }



    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;

    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;


    @GetMapping(value = "/chatClient/call")
    public String chatClientCall(@RequestParam String msg) {
        return deepseekChatClient.prompt().user(msg).call().content();

    }

    @GetMapping(value = "/chatClient/flux")
    public Flux<String> chatClientFlux(@RequestParam String msg) {
        return qwenChatClient.prompt().user(msg).stream().content();
    }

}
