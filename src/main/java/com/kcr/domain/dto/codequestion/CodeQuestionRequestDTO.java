package com.kcr.domain.dto.codequestion;

import com.kcr.domain.entity.CodeQuestion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* 게시글의 등록과 수정을 처리할 요청 클래스 */
@Getter
@Setter // (access = AccessLevel.PROTECTED)
public class CodeQuestionRequestDTO {
    private String title;
    private String writer;
    private String content;
    private String codeContent;

    /* 게시글 등록 */
    @Builder
    public CodeQuestionRequestDTO(String title, String writer, String content, String codeContent) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.codeContent = codeContent;
    }

    /* DTO -> Entity */
    public CodeQuestion toSaveEntity() {

        return CodeQuestion.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .codeContent(codeContent)
                .build();
    }
}