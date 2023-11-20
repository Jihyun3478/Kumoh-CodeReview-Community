package com.kcr.domain.dto.codequestion;

import com.kcr.domain.entity.CodeQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/* 게시글의 등록과 수정을 처리할 요청 클래스 */
@Getter
@AllArgsConstructor
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
public class CodeQuestionRequestDTO {
    private String title;
    private String writer;
    private String content;
    private String codeContent;
    private String createDate;
    private String modifiedDate;
    private Long likes;
    private Long views;
//        private Member member;

    /* 게시글 등록 */
    @Builder
    public CodeQuestionRequestDTO(String title, String writer, String content, String codeContent, String createDate, Long likes, Long views) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.codeContent = codeContent;
        this.createDate = createDate;
        this.likes = likes;
        this.views = views;
//            this.member = member;
    }

    /* DTO -> Entity */
    public CodeQuestion toSaveEntity() {

        return CodeQuestion.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .codeContent(codeContent)
                .likes(0L)
                .views(0L)
//                    .member(member)
                .build();
    }
}