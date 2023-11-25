package com.kcr.repository;

import com.kcr.domain.entity.ChatGPT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatGPTRepository extends JpaRepository<ChatGPT, Long> {
}
