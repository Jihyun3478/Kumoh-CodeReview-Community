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
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionComment> child = new ArrayList<>();

    public QuestionComment(String content,Long totalLikes, String writer, Question question_id){
        this.content = content;
        this.totalLikes = totalLikes;
        this.writer = writer;
        this.question=question_id;
    }

    public void updateParent(QuestionComment parent) {
        this.parent = parent;
    }
    public void updateQuestionComment(String content) {
        this.content = content;
    }
}