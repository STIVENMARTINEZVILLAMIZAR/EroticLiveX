package com.example.lujuria.chat.dto;

import com.example.lujuria.chat.entity.ChatMessage;
import java.time.Instant;

public record ChatMessageResponse(
    Long id,
    Long conversationId,
    Long senderId,
    String senderName,
    String content,
    Instant createdAt
) {

    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
            message.getId(),
            message.getConversation().getId(),
            message.getSender().getId(),
            message.getSender().getFirstName() + " " + message.getSender().getLastName(),
            message.getContent(),
            message.getCreatedAt()
        );
    }
}
