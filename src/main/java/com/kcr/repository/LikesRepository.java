package com.kcr.repository;

import com.kcr.domain.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l FROM Likes l WHERE l.question.id = :questionId")
    List<Likes> findAllWithQuestion(@Param("questionId") Long questionId);
    long countByQuestionId(Long questionId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.questionComment = :questionComment")
    void deleteByQuestionCommentId(@Param("questionComment") QuestionComment questionComment);

    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.codeQuestionComment = :codeQuestionComment")
    void deleteByCodeQuestionCommentId(@Param("codeQuestionComment") CodeQuestionComment codeQuestionComment);
    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.question = :question")
    void deleteByQuestionId(@Param("question") Question question);
    @Transactional
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.codeQuestion = :codeQuestion")
    void deleteByCodeQuestionId(@Param("codeQuestion") CodeQuestion codeQuestion);
}
