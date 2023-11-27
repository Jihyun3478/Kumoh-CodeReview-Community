package com.kcr.domain.dto.question;

import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

/* 게시글 정보를 리턴할 응답 클래스 */
/* Entity 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답 */
/* 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지 */
@Getter
@Setter
public class QuestionResponseDTO {
    private final Long id;
    private final String title;
    private final String writer;
    private final String content;
    private final String createDate;
    private final Long totalLikes;
    private final Long views;
    private Page<QuestionCommentResponseDTO> questionComments;
    private ChatGptResponse chatGPT;
    private Long totalComments;

    /* Entity -> DTO */
    @Builder
    public QuestionResponseDTO(Long id, String title, String writer, String content, String createDate, Long totalLikes, Long views) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createDate = createDate;
        this.totalLikes = totalLikes;
        this.views = views;

    }

    public QuestionResponseDTO(Question question) {
        this.id = question.getId();
        this.title = question.getTitle();
        this.writer = question.getWriter();
        this.content = question.getContent();
        this.createDate = question.getCreateDate();
        this.totalLikes = question.getTotalLikes();
        this.views = question.getViews();
//            this.memberId = question.getMember().getId();
    }
}