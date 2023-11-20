package com.kcr.domain.dto.questioncomment;

import com.kcr.domain.entity.QuestionComment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionCommentResponseDTO {
    private Long question_comment_id;
    private String content;
    private Long is_removed;
    private Long likes;
    private String writer;
    private String gptcontent;


    @Builder
    public QuestionCommentResponseDTO(Long question_comment_id, String content, Long is_removed, Long likes, String writer) {
        this.question_comment_id = question_comment_id;
        this.content = content;
        this.is_removed = is_removed;
        this.likes = likes;
        this.writer = writer;
    }

    public QuestionCommentResponseDTO(QuestionComment questionComment) {
        this.question_comment_id = questionComment.getId();
        this.content = questionComment.getContent();
        this.likes = questionComment.getLikes();
        this.writer = questionComment.getWriter();
    }
}
