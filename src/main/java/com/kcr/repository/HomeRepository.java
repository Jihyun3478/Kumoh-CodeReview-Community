package com.kcr.repository;

import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Question, Long> {
    Page<QuestionListResponseDTO> findTop5ByOrderByCreateDateDesc(Pageable pageable);
}
