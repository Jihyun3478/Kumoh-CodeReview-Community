package com.kcr.domain.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKES_ID")
    private Long id;

    //연관관계
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_COMMENT_ID")
    private QuestionComment questionComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CODEQUESTION_ID")
    private CodeQuestion codeQuestion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CODEQUESTION_COMMENT_ID")
    private CodeQuestionComment codeQuestionComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Likes(Question question, QuestionComment questionComment, CodeQuestion codeQuestion, CodeQuestionComment codeQuestionComment){
        this.question=question;
        this.questionComment=questionComment;
        this.codeQuestion=codeQuestion;
        this.codeQuestionComment=codeQuestionComment;
    }

    public void updateQuestion(Question question) {
        this.question= question;
    }

    public void updateCodeQuestion(CodeQuestion codeQuestion) {
        this.codeQuestion=codeQuestion;
    }

    public void updateQuestionComment(QuestionComment questionComment) {
        this.questionComment=questionComment;
    }

    public void updateCodeQuestionComment(CodeQuestionComment codeQuestionComment) {
        this.codeQuestionComment=codeQuestionComment;
    }
}
