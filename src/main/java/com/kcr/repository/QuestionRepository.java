package com.kcr.repository;

import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);
    Optional<Question> findByIdAndMemberId(Long questionId, Long memberId);
    Page<QuestionListResponseDTO> findTop5ByOrderByCreateDateDesc(Pageable pageable);

    Page<QuestionListResponseDTO> findByTitleContaining(String title, Pageable pageable);

    Page<QuestionListResponseDTO> findByWriterContaining(String writer, Pageable pageable);

    @Modifying
    @Query("update Question q set q.views = q.views + 1 where q.id=:id")
    void updateViews(@Param("id") Long id);
}
