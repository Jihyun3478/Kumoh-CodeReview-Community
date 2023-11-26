package com.kcr.repository;
import com.kcr.domain.entity.QuestionComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionCommentRepository extends JpaRepository<QuestionComment, Long> {
    Page<QuestionComment> findAll(Pageable pageable);
    List<QuestionComment> findAllByQuestionId(Long questionID);
    //부모댓글이 없을때(최상위 댓글일때)
    List<QuestionComment> findByQuestionIdAndParentIsNull(Long questionId);

    @Query("SELECT qc FROM QuestionComment qc WHERE qc.parent.id = :parentId")
    List<QuestionComment> findChildCommentsByParentId(@Param("parentId") Long parentId);

    @Query(value = "SELECT DISTINCT * FROM questioncomment qc WHERE qc.question_id = :questionId", nativeQuery = true)
    List<QuestionComment> findAllWithRepliesByQuestionId(@Param("questionId") Long questionId);

    // Pageable은 JPA의 표준 쿼리 메소드와 잘 통합되어 있지만, 네이티브 SQL 쿼리와 함께 사용할 경우에는 추가적인 작업이 필요할 수 있음
    @Query(value = "SELECT qc FROM QuestionComment qc WHERE qc.question.id = :questionId AND qc.parent.id IS NULL")
    Page<QuestionComment> findByQuestionIdWithPagination(@Param("questionId") Long questionId, Pageable pageable);

}



