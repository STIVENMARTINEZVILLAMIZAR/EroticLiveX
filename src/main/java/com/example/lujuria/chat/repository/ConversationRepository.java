package com.example.lujuria.chat.repository;

import com.example.lujuria.chat.entity.Conversation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
        select distinct conversation
        from Conversation conversation
        join conversation.participants participant
        where participant.user.id = :userId
        order by conversation.lastMessageAt desc
        """)
    List<Conversation> findAllVisibleForUser(Long userId);
}
