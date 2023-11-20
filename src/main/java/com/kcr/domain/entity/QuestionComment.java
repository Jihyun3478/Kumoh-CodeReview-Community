package com.kcr.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@Table(name = "questioncomment")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
@AllArgsConstructor
public class QuestionComment {

    @Id @GeneratedValue
    @Column(name = "QUESTION_COMMENT_ID")
    private Long id;

    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Long likes;
    private boolean isRemoved;

    /* 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private QuestionComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private final List<QuestionComment> child = new ArrayList<>();

    public QuestionComment(String content,Long likes, String writer) {
        this.content = content;
        this.likes = likes;
        this.writer = writer;
    }

    // Getters and setters

    public void updateQuestionComment(String content) {
        this.content = content;
    }
}
