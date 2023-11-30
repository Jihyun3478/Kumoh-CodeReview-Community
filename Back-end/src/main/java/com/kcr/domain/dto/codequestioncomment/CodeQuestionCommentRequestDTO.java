package com.kcr.domain.dto.codequestioncomment;

import com.kcr.domain.entity.CodeQuestion;
import com.kcr.domain.entity.CodeQuestionComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CodeQuestionCommentRequestDTO {
    private Long id;
    private String content;
    private String codeContent;
    private Long totalLikes;
    private String writer;
    private CodeQuestion codeQuestion;
    private String createDate;
    private CodeQuestionComment parentComment;

    @Builder
    public CodeQuestionCommentRequestDTO(String content,String codeContent, Long totalLikes, String writer) {
        this.content = content;
        this.codeContent=codeContent;
        this.totalLikes = totalLikes;
        this.writer = writer;
    }
    /* DTO -> Entity */
    public CodeQuestionComment toSaveEntity() {
        return CodeQuestionComment.builder()
                .id(id)
                .content(content)
                .codeContent(codeContent)
                .writer(writer)
                .totalLikes(0L)
                .codeQuestion(codeQuestion)
                .build();
    }
}
