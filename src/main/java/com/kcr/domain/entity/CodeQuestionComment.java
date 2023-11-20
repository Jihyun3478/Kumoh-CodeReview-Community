package com.kcr.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "codequestioncomment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeQuestionComment {

    @Id @GeneratedValue
    @Column(name = "CODE_QUESTION_COMMENT_ID")
    private Long id;

    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Long likes;
    private boolean isRemoved;

    /* 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CODE_QUESTION_ID")
    private CodeQuestion codeQuestion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private QuestionComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<QuestionComment> child = new ArrayList<>();
}
