package com.kcr.domain.dto.questioncomment;

import com.kcr.domain.entity.Question;
import com.kcr.domain.entity.QuestionComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuestionCommentRequestDTO {
    private Long id;
    private String content;
    private Long totalLikes;
    private String writer;
    private Question question;
    private String createDate;
    private QuestionComment parentComment;
    @Builder
    public QuestionCommentRequestDTO(String content,Long totalLikes, String writer) {
        this.content = content;
        this.totalLikes = totalLikes;
        this.writer = writer;
    }
    /* DTO -> Entity */
    public QuestionComment toSaveEntity() {
        return QuestionComment.builder()
                .id(id)
                .content(content)
                .writer(writer)
                .totalLikes(0L)
                .question(question)
                .build();
    }
}
