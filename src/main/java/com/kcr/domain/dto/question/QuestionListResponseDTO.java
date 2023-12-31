package com.kcr.domain.dto.question;

import com.kcr.domain.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* 게시글 정보를 리턴할 응답 클래스 */
/* Entity 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답 */
/* 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지 */
@Getter
public class QuestionListResponseDTO {
    private final Long id;
    private final String title;
    private final String writer;
    private final String createDate;
    private final Long totalLikes;
    private final Long views;
    @Setter
    private int totalComments;

    /* Entity -> DTO */
    @Builder
    public QuestionListResponseDTO(Long id, String title, String writer, String createDate, Long totalLikes, Long views, int totalComments) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createDate = createDate;
        this.totalLikes = totalLikes;
        this.views = views;
        this.totalComments = totalComments;
    }

    public QuestionListResponseDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.writer = question.getWriter();
        this.createDate = question.getCreateDate();
        this.totalLikes = question.getTotalLikes();
        this.views = question.getViews();
    }
}