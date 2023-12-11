package com.kcr.repository;

import com.kcr.domain.entity.CodeQuestionComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeQuestionCommentRepository extends JpaRepository<CodeQuestionComment, Long> {
    List<CodeQuestionComment> findAllByCodeQuestionId(Long codeQuestionId);

    @Query("SELECT qc FROM CodeQuestionComment qc WHERE qc.parent.id = :parentId")
    List<CodeQuestionComment> findChildCommentsByParentId(@Param("parentId") Long parentId);
    @Query(value = "SELECT cm FROM CodeQuestionComment cm WHERE cm.codeQuestion.id = :codeQuestionId AND cm.parent.id IS NULL")
    Page<CodeQuestionComment> findByCodeQuestionIdWithPagination(@Param("codeQuestionId") Long codeQuestionId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CodeQuestionComment c WHERE c.codeQuestion.id = :codeQuestionId")
    Long countByCodeQuestionId(@Param("codeQuestionId") Long codeQuestionId);

}
