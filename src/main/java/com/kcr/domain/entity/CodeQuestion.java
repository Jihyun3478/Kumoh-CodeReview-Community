package com.kcr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcr.domain.dto.codequestion.CodeQuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(columnDefinition = "integer default 0")
    private Long totalLikes = 0L;

    @OneToMany(mappedBy = "codeQuestion")
    @Builder.Default
    @JsonIgnore
    private List<Likes> likes= new ArrayList<>();

    @Column(columnDefinition = "integer default 0")
    private Long views;

    /* 연관관계 */
    @OneToOne(mappedBy = "codeQuestion")
    private Member member;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_ID")
//    private Member member;

    @OneToMany(mappedBy = "codeQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_id is null")
    @JsonIgnore
    private final List<CodeQuestionComment> codeQuestionComments = new ArrayList<>();

    @OneToMany(mappedBy = "codeQuestion")
    private final List<HashTag> hashTags = new ArrayList<>();

    /* 생성자 */
    public CodeQuestion(String title, String writer, String content, String codeContent, Long totalLikes, Long views) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.codeContent = codeContent;
        this.totalLikes = totalLikes;
        this.views = views;
    }

    public CodeQuestion(CodeQuestionRequestDTO requestDto) {
        this.title = requestDto.getTitle();
        this.writer = requestDto.getWriter();
        this.content = requestDto.getContent();
        this.codeContent = requestDto.getCodeContent();
    }

    /* 비즈니스 로직 */
    /* 게시글 수정 */
    public void updateCodeQuestion(String title, String content, String codeContent) {
        this.title = title;
        this.content = content;
        this.codeContent = codeContent;
    }
}
