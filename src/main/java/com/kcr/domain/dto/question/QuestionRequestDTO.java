package com.kcr.domain.dto.question;

import com.kcr.domain.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 게시글의 등록과 수정을 처리할 요청 클래스 */
@Getter
@Setter
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
public class QuestionRequestDTO {
    private String title;
    private String writer;
    private String content;

    /* 게시글 등록 */
    @Builder
    public QuestionRequestDTO(String title, String writer, String content) {
        this.title = title;
        this.writer = writer;
        this.content = content;
    }

    /* DTO -> Entity */
    public Question toSaveEntity() {

        return Question.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();
    }
}