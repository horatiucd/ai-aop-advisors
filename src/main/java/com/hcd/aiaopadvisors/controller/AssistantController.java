package com.hcd.aiaopadvisors.controller;

import com.hcd.aiaopadvisors.advisor.TokenUsageAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assistant")
public class AssistantController {

    private final ChatClient chatClient;

    public AssistantController(ChatClient.Builder builder, ChatMemory chatMemory) {
        chatClient = builder
                .defaultSystem("You are a helpful AI assistant, provide short, focused answers.")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).order(0).build(),
                        TokenUsageAdvisor.builder().order(1).build(),
                        SimpleLoggerAdvisor.builder().order(2).build()
                ).build();
    }

    @RequestMapping("/ask")
    public String ask(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}
