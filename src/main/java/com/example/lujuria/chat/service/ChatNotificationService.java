package com.example.lujuria.chat.service;

import com.example.lujuria.chat.dto.ChatMessageResponse;
import java.util.List;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatNotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyConversation(Long conversationId, List<String> participantEmails, ChatMessageResponse response) {
        messagingTemplate.convertAndSend("/topic/conversations/" + conversationId, response);
        for (String email : participantEmails) {
            messagingTemplate.convertAndSendToUser(email, "/queue/messages", response);
        }
    }
}
