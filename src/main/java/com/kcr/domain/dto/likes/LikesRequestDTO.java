package com.kcr.domain.dto.likes;

import com.kcr.domain.entity.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LikesRequestDTO {
    private Long id;
    private Question question;
    private QuestionComment questionComment;

    private CodeQuestion codeQuestion;
    private CodeQuestionComment codeQuestionComment;

    @Builder
    public LikesRequestDTO(Question question, QuestionComment questionComment, CodeQuestion codeQuestion, CodeQuestionComment codeQuestionComment) {
        this.question = question;
        this.questionComment = questionComment;
        this.codeQuestion=codeQuestion;
        this.codeQuestionComment=codeQuestionComment;
    }
    /* DTO -> Entity */
    public Likes toSaveEntity() {
        return Likes.builder()
                .question(question)
                .questionComment(questionComment)
                .codeQuestion(codeQuestion)
                .codeQuestionComment(codeQuestionComment)
                .build();
    }
}
