package com.kcr.domain.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
@AllArgsConstructor
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QUESTION_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Long likes;

    @Column(columnDefinition = "integer default 0")
    private Long views;

    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private final List<QuestionComment> questionComments = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private final List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "question")
    private final List<HashTag> hashTags = new ArrayList<>();


    /* 생성자 */
    public Question(String title, String writer, String content, Long likes, Long views) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.likes = likes;
        this.views = views;
    }

    /* 비즈니스 로직 */
    /* 게시글 수정 */
    public void updateQuestion(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
