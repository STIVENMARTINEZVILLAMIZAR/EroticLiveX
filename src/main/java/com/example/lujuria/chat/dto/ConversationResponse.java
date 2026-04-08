package com.example.lujuria.chat.dto;

import com.example.lujuria.chat.entity.Conversation;
import java.time.Instant;
import java.util.Set;

public record ConversationResponse(
    Long id,
    String type,
    String subject,
    Instant lastMessageAt,
    Set<Long> participantIds
) {

    public static ConversationResponse from(Conversation conversation) {
        return new ConversationResponse(
            conversation.getId(),
            conversation.getType().name(),
            conversation.getSubject(),
            conversation.getLastMessageAt(),
            conversation.getParticipants().stream()
                .map(participant -> participant.getUser().getId())
                .collect(java.util.stream.Collectors.toSet())
        );
    }
}
