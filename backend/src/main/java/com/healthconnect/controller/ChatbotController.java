package com.healthconnect.controller;

import com.healthconnect.chatbot.dto.ChatSessionDto;
import com.healthconnect.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/start")
    public ResponseEntity<ChatSessionDto> startSession() {
        return ResponseEntity.ok(chatbotService.startNewSession());
    }

    @PostMapping("/message")
    public ResponseEntity<ChatSessionDto> sendMessage(@RequestBody Map<String, String> body) {
        String sessionId = body.get("sessionId");
        String message = body.get("message");
        return ResponseEntity.ok(chatbotService.processUserMessage(sessionId, message));
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ChatSessionDto> getSession(@PathVariable String sessionId) {
        ChatSessionDto session = chatbotService.getSession(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }
}
