package com.kcr.repository;

import com.kcr.domain.entity.CodeQuestionComment;
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
    @Query(value = "SELECT * FROM codequestioncomment qc WHERE qc.code_question_id = :codeQuestionId LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<CodeQuestionComment> findAllWithRepliesByCodeQuestionId(@Param("codeQuestionId") Long codeQuestionId,
                                                                 @Param("limit") int limit,
                                                                 @Param("offset") int offset);

}