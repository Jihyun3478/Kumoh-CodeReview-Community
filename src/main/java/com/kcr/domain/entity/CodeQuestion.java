package com.kcr.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "codequestion")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CodeQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "CODE_QUESTION_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String codeContent;

    private Long likes;

    @Column(columnDefinition = "integer default 0")
    private Long views;

    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private Member member;

    @OneToMany(mappedBy = "codeQuestion", cascade = CascadeType.REMOVE)
    private final List<CodeQuestionComment> codeQuestionComments = new ArrayList<>();

    @OneToMany(mappedBy = "codeQuestion")
    private final List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "codeQuestion")
    private final List<HashTag> hashTags = new ArrayList<>();

    /* 생성자 */
    public CodeQuestion(String title, String writer, String content, String codeContent, Long likes, Long views) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.codeContent = codeContent;
        this.likes = likes;
        this.views = views;
    }

    /* 비즈니스 로직 */
    /* 게시글 수정 */
    public void updateQuestion(String title, String content, String codeContent) {
        this.title = title;
        this.content = content;
        this.codeContent = codeContent;
    }
}
