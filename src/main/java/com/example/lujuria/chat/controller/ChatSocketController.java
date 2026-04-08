package com.example.lujuria.chat.controller;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.chat.dto.ChatMessageResponse;
import com.example.lujuria.chat.dto.SocketChatMessageRequest;
import com.example.lujuria.chat.service.ChatNotificationService;
import com.example.lujuria.chat.service.ChatService;
import java.security.Principal;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatSocketController {

    private final ChatService chatService;
    private final ChatNotificationService chatNotificationService;

    public ChatSocketController(ChatService chatService, ChatNotificationService chatNotificationService) {
        this.chatService = chatService;
        this.chatNotificationService = chatNotificationService;
    }

    @MessageMapping("/chat/{conversationId}")
    public void sendMessage(
        @DestinationVariable Long conversationId,
        SocketChatMessageRequest request,
        Principal principal
    ) {
        if (!(principal instanceof org.springframework.security.core.Authentication authentication)
            || !(authentication.getPrincipal() instanceof AppUserPrincipal appUserPrincipal)) {
            throw new IllegalStateException("Conexion WebSocket sin autenticacion valida.");
        }

        ChatMessageResponse response = chatService.sendMessage(appUserPrincipal, conversationId, request.content());
        chatNotificationService.notifyConversation(conversationId, chatService.participantEmails(conversationId), response);
    }
}
