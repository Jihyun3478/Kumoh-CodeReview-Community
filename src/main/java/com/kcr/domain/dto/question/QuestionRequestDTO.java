package com.kcr.domain.dto.question;

import com.kcr.domain.entity.Question;
import com.kcr.domain.entity.Member;
import lombok.*;

/* 게시글의 등록과 수정을 처리할 요청 클래스 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
public class QuestionRequestDTO {
    private String title;
    private Member member;
    private String writer;
    private String content;
    private String createDate;
    private String modifiedDate;
    private Long likes;
    private Long views;

    /* 게시글 등록 */
    @Builder
    public QuestionRequestDTO(String title, Member member, String content, String createDate, Long likes, Long views) {
        this.title = title;
        this.member = member;
        this.content = content;
        this.createDate = createDate;
        this.likes = likes;
        this.views = views;
    }

    /* DTO -> Entity */
    public Question toSaveEntity() {

        return Question.builder()
                .title(title)
                .member(member)
//                .writer(writer)
                .content(content)
                .likes(0L)
                .views(0L)
                .build();
    }
}