package com.kcr.repository;

import com.kcr.domain.entity.QuestionComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommentRepository extends JpaRepository<QuestionComment, Long> {
    Page<QuestionComment> findAll(Pageable pageable);

    //공감
    @Modifying
    @Query("update QuestionComment q set q.likes = q.likes + 1 where q.id=:id")
    void updateLikes(@Param("id")Long id);

    //공감취소
    @Modifying
    @Query("update QuestionComment q set q.likes = q.likes -1 where q.id=:id")
    void cancelLikes(@Param("id")Long id);
}


