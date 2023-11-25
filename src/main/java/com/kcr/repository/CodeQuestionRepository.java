package com.kcr.repository;

import com.kcr.domain.dto.codequestion.CodeQuestionListResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeQuestionRepository extends JpaRepository<CodeQuestion, Long> {

    Page<CodeQuestion> findAll(Pageable pageable);
    Optional<CodeQuestion> findByIdAndMemberId(Long questionId, Long memberId);
    Page<CodeQuestionListResponseDTO> findTop5ByOrderByCreateDateDesc(Pageable pageable);

    Page<CodeQuestionListResponseDTO> findByTitleContaining(String title, Pageable pageable);

    Page<CodeQuestionListResponseDTO> findByWriterContaining(String writer, Pageable pageable);

    @Modifying
    @Query("update CodeQuestion cq set cq.views = cq.views + 1 where cq.id=:id")
    void updateViews(@Param("id") Long id);
}
