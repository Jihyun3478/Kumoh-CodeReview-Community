package com.kcr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "questioncomment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuestionComment extends BaseTimeEntity{
    @Id @GeneratedValue
    @Column(name = "QUESTION_COMMENT_ID")
    private Long id;
    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "integer default 0")
    private Long totalLikes = 0L;

    @OneToMany(mappedBy = "questionComment")
    @Builder.Default
    @JsonIgnore
    private List<Likes> likes= new ArrayList<>();

    /* 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private QuestionComment parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<QuestionComment> child = new ArrayList<>();

    @OneToOne(mappedBy = "questionComment")
    private Member member;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_ID")
//    private Member member;

    public QuestionComment(String content,Long totalLikes, String writer, Question question_id){
        this.content = content;
        this.totalLikes = totalLikes;
        this.writer = writer;
        this.question=question_id;
    }

    public QuestionComment(QuestionCommentRequestDTO questionCommentRequestDTO){
        this.content = questionCommentRequestDTO.getContent();
        this.totalLikes = questionCommentRequestDTO.getTotalLikes();
        this.writer = questionCommentRequestDTO.getWriter();
        this.question=questionCommentRequestDTO.getQuestion();
    }

    public void updateParent(QuestionComment parent) {
        this.parent = parent;
    }
    public void updateQuestionComment(String content) {
        this.content = content;
    }
}