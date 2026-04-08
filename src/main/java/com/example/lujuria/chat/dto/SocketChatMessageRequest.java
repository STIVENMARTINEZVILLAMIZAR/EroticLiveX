package com.example.lujuria.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SocketChatMessageRequest(
    @NotBlank @Size(max = 2000) String content
) {
}
