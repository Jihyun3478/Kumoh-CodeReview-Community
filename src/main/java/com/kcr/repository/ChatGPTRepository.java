package com.kcr.repository;

import com.kcr.domain.entity.ChatGPT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatGPTRepository extends JpaRepository<ChatGPT, Long> {
    @Modifying
    @Query("update ChatGPT gpt set gpt.gptContent = :content where gpt.id=:id")
    void updateContent(@Param("id") Long id, String content);
}
