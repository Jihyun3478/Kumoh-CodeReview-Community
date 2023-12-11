package com.kcr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
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

    @Column(columnDefinition = "integer default 0")
    private Long totalLikes = 0L;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

    @NotEmpty
    @Column(columnDefinition = "LONGTEXT", length = 2000)
    private String content;


    @Column(columnDefinition = "integer default 0")
    private Long views = 0L;

    /* 연관관계 */
    @OneToOne(mappedBy = "question")
    private Member member;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MEMBER_ID")
//    private Member member;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "parent_id is null")
    @JsonIgnore
    private List<QuestionComment> questionComments;

    @OneToMany(mappedBy = "question")
    @Builder.Default
    @JsonIgnore
    private List<Likes> likes= new ArrayList<>();

    @OneToMany(mappedBy = "question")
    @Builder.Default
    @JsonIgnore
    private List<HashTag> hashTags = new ArrayList<>();

    @OneToOne(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatGPT chatGPT;

    /* 생성자 */
    public Question(Long id, String title, String writer, String content, Long totalLikes, Long views) {
        this.id= id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.totalLikes = totalLikes;
        this.views = views;
    }

    public Question(QuestionRequestDTO requestDto) {
        this.title = requestDto.getTitle();
        this.writer = requestDto.getWriter();
        this.content = requestDto.getContent();
    }

    /* 비즈니스 로직 */
    /* 게시글 수정 */
    public void updateQuestion(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

//package com.kcr.domain.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.kcr.domain.dto.question.QuestionRequestDTO;
//import lombok.*;
//import org.hibernate.annotations.Where;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Setter
//@Getter
//@Builder
//@Table(name = "question")
//@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
//@AllArgsConstructor
//public class Question extends BaseTimeEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "QUESTION_ID")
//    private Long id;
//
//    @Column(columnDefinition = "integer default 0")
//    private Long totalLikes = 0L;
//
//    @Column(nullable = false)
//    private String title;
//
//    @Column(nullable = false)
//    private String writer;
//
//    @NotEmpty
//    @Column(columnDefinition = "LONGTEXT", length = 2000)
//    private String content;
//
//
//    @Column(columnDefinition = "integer default 0")
//    private Long views;
//
//    /* 연관관계 */
//    @OneToOne(mappedBy = "question")
//    private Member member;
//
////    @ManyToOne(fetch = FetchType.LAZY)
////    @JoinColumn(name = "MEMBER_ID")
////    private Member member;
//
////    @OneToMany(mappedBy = "codeQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
////    @Where(clause = "parent_id is null")
////    @JsonIgnore
//    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL, orphanRemoval = true)
//    @Where(clause = "parent_id is null")
//    @JsonIgnore
//    private List<QuestionComment> questionComments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "question")
//    @Builder.Default
//    @JsonIgnore
//    private List<Likes> likes= new ArrayList<>();
//
//    @OneToMany(mappedBy = "question")
//    @Builder.Default
//    @JsonIgnore
//    private List<HashTag> hashTags = new ArrayList<>();
//
//    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private ChatGPT chatGPT;
//
//    /* 생성자 */
//    public Question(Long id, String title, String writer, String content, Long totalLikes, Long views) {
//        this.id= id;
//        this.title = title;
//        this.writer = writer;
//        this.content = content;
//        this.totalLikes = totalLikes;
//        this.views = views;
//    }
//
//    public Question(QuestionRequestDTO requestDto) {
//        this.title = requestDto.getTitle();
//        this.writer = requestDto.getWriter();
//        this.content = requestDto.getContent();
//    }
//
//    /* 비즈니스 로직 */
//    /* 게시글 수정 */
//    public void updateQuestion(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
//}
