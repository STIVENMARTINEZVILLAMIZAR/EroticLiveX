package com.example.lujuria.chat.dto;

import com.example.lujuria.chat.entity.ConversationType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateConversationRequest(
    @NotNull ConversationType type,
    @Size(max = 200) String subject,
    @NotEmpty Set<Long> participantIds
) {
}
