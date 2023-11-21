package com.kcr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
@AllArgsConstructor
@Builder
public class QuestionComment {
    @Id @GeneratedValue
    @Column(name = "QUESTION_COMMENT_ID")
    private Long id;
    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Long likes;
    /* 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private QuestionComment parent;
    @Builder.Default
    //@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
/*    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)*/
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionComment> child = new ArrayList<>();
    public QuestionComment(String content,Long likes, String writer, Question question_id){
        this.content = content;
        this.likes = likes;
        this.writer = writer;
        this.question=question_id;
    }

    @Builder
    public QuestionComment( String content, Long likes, Question question, QuestionComment parent) {
        this.content = content;
        this.likes = likes;
        this.question = question;
        this.parent = parent;
    }
    public void updateParent(QuestionComment parent) {
        this.parent = parent;
    }
    public void updateQuestionComment(String content) {
        this.content = content;
    }

}
