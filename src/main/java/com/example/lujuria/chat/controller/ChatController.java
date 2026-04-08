package com.example.lujuria.chat.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.chat.dto.ChatMessageResponse;
import com.example.lujuria.chat.dto.ConversationResponse;
import com.example.lujuria.chat.dto.CreateConversationRequest;
import com.example.lujuria.chat.dto.SendMessageRequest;
import com.example.lujuria.chat.service.ChatNotificationService;
import com.example.lujuria.chat.service.ChatService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat/conversations")
public class ChatController {

    private final ChatService chatService;
    private final ChatNotificationService chatNotificationService;

    public ChatController(ChatService chatService, ChatNotificationService chatNotificationService) {
        this.chatService = chatService;
        this.chatNotificationService = chatNotificationService;
    }

    @PostMapping
    public ConversationResponse createConversation(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @Valid @RequestBody CreateConversationRequest request
    ) {
        return chatService.createConversation(principal, request);
    }

    @GetMapping
    public List<ConversationResponse> myConversations(@AuthenticationPrincipal AppUserPrincipal principal) {
        return chatService.myConversations(principal);
    }

    @GetMapping("/{conversationId}/messages")
    public List<ChatMessageResponse> messages(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @PathVariable Long conversationId
    ) {
        return chatService.conversationMessages(principal, conversationId);
    }

    @PostMapping("/{conversationId}/messages")
    public ChatMessageResponse sendMessage(
        @AuthenticationPrincipal AppUserPrincipal principal,
        @PathVariable Long conversationId,
        @Valid @RequestBody SendMessageRequest request
    ) {
        ChatMessageResponse response = chatService.sendMessage(principal, conversationId, request.content());
        chatNotificationService.notifyConversation(conversationId, chatService.participantEmails(conversationId), response);
        return response;
    }
}
