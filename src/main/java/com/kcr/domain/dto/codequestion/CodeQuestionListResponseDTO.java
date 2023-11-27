package com.kcr.domain.dto.codequestion;

import com.kcr.domain.entity.CodeQuestion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CodeQuestionListResponseDTO {
    private final Long id;
    private final String title;
    private final String writer;
    private final String createDate;
    private final Long totalLikes;
    private final Long views;

    /* Entity -> DTO */
    @Builder
    public CodeQuestionListResponseDTO(Long id, String title, String writer, String createDate, Long totalLikes, Long views) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createDate = createDate;
        this.totalLikes = totalLikes;
        this.views = views;
    }

    public CodeQuestionListResponseDTO(CodeQuestion codeQuestion) {
        this.id = codeQuestion.getId();
        this.title = codeQuestion.getTitle();
        this.writer = codeQuestion.getWriter();
        this.createDate = codeQuestion.getCreateDate();
        this.totalLikes = codeQuestion.getTotalLikes();
        this.views = codeQuestion.getViews();
    }
}