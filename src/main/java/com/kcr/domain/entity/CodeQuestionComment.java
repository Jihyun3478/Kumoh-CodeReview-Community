package com.kcr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Table(name = "codequestioncomment")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
@AllArgsConstructor //얘 안넣어주면 @Builder annotation 에러뜸
public class CodeQuestionComment extends BaseTimeEntity{

    @Id @GeneratedValue
    @Column(name = "CODE_QUESTION_COMMENT_ID")
    private Long id;

    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT", name = "CODE_CONTENT")
    private String codeContent;

    @Column(columnDefinition = "integer default 0")
    private Long totalLikes = 0L;

    @OneToMany(mappedBy = "codeQuestionComment")
    @Builder.Default
    @JsonIgnore
    private List<Likes> likes= new ArrayList<>();

    /* 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CODE_QUESTION_ID")
    private CodeQuestion codeQuestion;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PARENT_ID")
    private CodeQuestionComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CodeQuestionComment> child = new ArrayList<>();

    public CodeQuestionComment(String content, String codeContent,Long totalLikes, String writer, CodeQuestion codeQuestion_id){
        this.content = content;
        this.codeContent = codeContent;
        this.totalLikes=totalLikes;
        this.writer = writer;
        this.codeQuestion=codeQuestion_id;
    }

    public void updateCodeQuestionComment(String content) {
        this.content = content;
    }

    public void updateParent(CodeQuestionComment codeQuestionComment) {
        this.parent=codeQuestionComment;
    }
}
