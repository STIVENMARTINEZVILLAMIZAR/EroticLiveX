package com.example.lujuria.chat.service;

import com.example.lujuria.auth.security.AppUserPrincipal;
import com.example.lujuria.chat.dto.ChatMessageResponse;
import com.example.lujuria.chat.dto.ConversationResponse;
import com.example.lujuria.chat.dto.CreateConversationRequest;
import com.example.lujuria.chat.entity.ChatMessage;
import com.example.lujuria.chat.entity.Conversation;
import com.example.lujuria.chat.entity.ConversationParticipant;
import com.example.lujuria.chat.repository.ChatMessageRepository;
import com.example.lujuria.chat.repository.ConversationParticipantRepository;
import com.example.lujuria.chat.repository.ConversationRepository;
import com.example.lujuria.common.BusinessRuleException;
import com.example.lujuria.common.ResourceNotFoundException;
import com.example.lujuria.user.entity.AppUser;
import com.example.lujuria.user.service.UserService;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository conversationParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatService(
        ConversationRepository conversationRepository,
        ConversationParticipantRepository conversationParticipantRepository,
        ChatMessageRepository chatMessageRepository,
        UserService userService
    ) {
        this.conversationRepository = conversationRepository;
        this.conversationParticipantRepository = conversationParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
    }

    @Transactional
    public ConversationResponse createConversation(AppUserPrincipal principal, CreateConversationRequest request) {
        AppUser currentUser = userService.getRequiredUser(principal.getUserId());
        Set<Long> participantIds = new LinkedHashSet<>(request.participantIds());
        participantIds.add(currentUser.getId());

        Conversation conversation = new Conversation();
        conversation.setType(request.type());
        conversation.setSubject(request.subject());
        Conversation savedConversation = conversationRepository.save(conversation);

        for (Long participantId : participantIds) {
            AppUser participantUser = userService.getRequiredUser(participantId);
            ConversationParticipant participant = new ConversationParticipant();
            participant.setConversation(savedConversation);
            participant.setUser(participantUser);
            conversationParticipantRepository.save(participant);
            savedConversation.getParticipants().add(participant);
        }

        return ConversationResponse.from(savedConversation);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponse> myConversations(AppUserPrincipal principal) {
        return conversationRepository.findAllVisibleForUser(principal.getUserId()).stream()
            .map(ConversationResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> conversationMessages(AppUserPrincipal principal, Long conversationId) {
        ensureParticipant(conversationId, principal.getUserId());
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).stream()
            .map(ChatMessageResponse::from)
            .toList();
    }

    @Transactional
    public ChatMessageResponse sendMessage(AppUserPrincipal principal, Long conversationId, String content) {
        AppUser sender = userService.getRequiredUser(principal.getUserId());
        Conversation conversation = getConversationIfParticipant(conversationId, sender.getId());

        if (content == null || content.isBlank()) {
            throw new BusinessRuleException("El contenido del mensaje no puede estar vacio.");
        }

        ChatMessage message = new ChatMessage();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content.trim());
        ChatMessage savedMessage = chatMessageRepository.save(message);

        conversation.setLastMessageAt(Instant.now());
        return ChatMessageResponse.from(savedMessage);
    }

    @Transactional(readOnly = true)
    public List<String> participantEmails(Long conversationId) {
        return conversationParticipantRepository.findByConversationId(conversationId).stream()
            .map(participant -> participant.getUser().getEmail())
            .toList();
    }

    private void ensureParticipant(Long conversationId, Long userId) {
        if (!conversationParticipantRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            throw new BusinessRuleException("No perteneces a esta conversacion.");
        }
    }

    private Conversation getConversationIfParticipant(Long conversationId, Long userId) {
        ensureParticipant(conversationId, userId);
        return conversationRepository.findById(conversationId)
            .orElseThrow(() -> new ResourceNotFoundException("Conversacion no encontrada."));
    }
}
