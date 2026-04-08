package com.example.lujuria.chat.repository;

import com.example.lujuria.chat.entity.ConversationParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, Long> {

    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);

    List<ConversationParticipant> findByConversationId(Long conversationId);
}
