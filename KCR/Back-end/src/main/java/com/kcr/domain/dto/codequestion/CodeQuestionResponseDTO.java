package com.kcr.domain.dto.codequestion;

import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

/* 게시글 정보를 리턴할 응답 클래스 */
/* Entity 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답 */
/* 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지 */
@Getter
@Setter //
public class CodeQuestionResponseDTO {
    private final Long id;
    private final String title;
    private final String writer;
    private final String content;
    private final String codeContent;
    private final String createDate;
    private final Long totalLikes;
    private final Long views;
    private Page<CodeQuestionCommentResponseDTO> codeQuestionComment;
    private Long totalComments;

    /* Entity -> DTO */
    @Builder
    public CodeQuestionResponseDTO(Long id, String title, String writer, String content, String codeContent, String createDate, Long totalLikes, Long views) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.codeContent = codeContent;
        this.createDate = createDate;
        this.totalLikes = totalLikes;
        this.views = views;
    }

    public CodeQuestionResponseDTO(CodeQuestion codeQuestion) {
        this.id = codeQuestion.getId();
        this.title = codeQuestion.getTitle();
        this.writer = codeQuestion.getWriter();
        this.content = codeQuestion.getContent();
        this.codeContent = codeQuestion.getCodeContent();
        this.createDate = codeQuestion.getCreateDate();
        this.totalLikes = codeQuestion.getTotalLikes();
        this.views = codeQuestion.getViews();
    }

    public CodeQuestionResponseDTO(CodeQuestion codeQuestion, Long total) {
        this.id = codeQuestion.getId();
        this.title = codeQuestion.getTitle();
        this.writer = codeQuestion.getWriter();
        this.content = codeQuestion.getContent();
        this.codeContent = codeQuestion.getCodeContent();
        this.createDate = codeQuestion.getCreateDate();
        this.totalLikes = codeQuestion.getTotalLikes();
        this.views = codeQuestion.getViews();
        this.totalComments = total;
    }
}