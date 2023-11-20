package com.kcr.domain.dto.question;

import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Question;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/* 게시글 정보를 리턴할 응답 클래스 */
/* Entity 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답 */
/* 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지 */
@Getter
public class QuestionResponseDTO {
    private final Long id;
    private final String title;
    private final String writer;
    private final String content;
    private final String createDate;
    private final Long likes;
    private final Long views;
    List<QuestionCommentResponseDTO> questionComments;
//        private final Long memberId;

    /* Entity -> DTO */
    @Builder
    public QuestionResponseDTO(Long id, String title, String writer, String content, String createDate, Long likes, Long views) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createDate = createDate;
        this.likes = likes;
        this.views = views;
    }

    public QuestionResponseDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.writer = question.getWriter();
        this.content = question.getContent();
        this.createDate = question.getCreateDate();
        this.likes = question.getLikes();
        this.views = question.getViews();
        this.questionComments = question.getQuestionComments().stream().map(QuestionCommentResponseDTO::new).collect(Collectors.toList());
//            this.memberId = question.getMember().getId();
    }
}