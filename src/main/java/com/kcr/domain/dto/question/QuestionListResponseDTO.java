package com.kcr.domain.dto.question;

import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Question;
import lombok.Builder;
import lombok.Getter;

/* 게시글 정보를 리턴할 응답 클래스 */
/* Entity 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답 */
/* 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지 */
@Getter
public class QuestionListResponseDTO {
    private final Long id;
    private final String title;
    private final Member member;
//    private final String writer;
    private final String createDate;
    private final Long likes;
    private final Long views;

    /* Entity -> DTO */
    @Builder
    public QuestionListResponseDTO(Long id, String title, Member member, String createDate, Long likes, Long views) {
        this.id = id;
        this.title = title;
        this.member = member;
//        this.writer = writer;
        this.createDate = createDate;
        this.likes = likes;
        this.views = views;
    }

    public QuestionListResponseDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.member = getMember();
//        this.writer = question.getWriter();
        this.createDate = question.getCreateDate();
        this.likes = question.getLikes();
        this.views = question.getViews();
    }
}