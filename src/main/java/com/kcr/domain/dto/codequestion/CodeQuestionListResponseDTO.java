package com.kcr.domain.dto.codequestion;

import com.kcr.domain.entity.CodeQuestion;
import lombok.Builder;
import lombok.Getter;

/* 게시글 정보를 리턴할 응답 클래스 */
/* Entity 클래스를 생성자 파라미터로 받아 데이터를 DTO로 변환하여 응답 */
/* 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티 간의 무한 참조 방지 */
@Getter
public class CodeQuestionListResponseDTO {
    private final Long id;
    private final String title;
    private final String writer;
    private final String createDate;
    private final Long totalLikes;
    private final Long views;
    private final int totalComments;
//        private final Long memberId;

    /* Entity -> DTO */
    @Builder
    public CodeQuestionListResponseDTO(Long id, String title, String writer, String createDate, Long totalLikes, Long views, int totalComments) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createDate = createDate;
        this.totalLikes = totalLikes;
        this.views = views;
        this.totalComments=totalComments;
    }

    public CodeQuestionListResponseDTO(CodeQuestion codeQuestion) {
        this.id = codeQuestion.getId();
        this.title = codeQuestion.getTitle();
        this.writer = codeQuestion.getWriter();
        this.createDate = codeQuestion.getCreateDate();
        this.totalLikes = codeQuestion.getTotalLikes();
        this.views = codeQuestion.getViews();
        this.totalComments=codeQuestion.getCodeQuestionComments().size();
//            this.memberId = question.getMember().getId();
    }
}
//package com.kcr.domain.dto.codequestion;
//
//import com.kcr.domain.entity.CodeQuestion;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//public class CodeQuestionListResponseDTO {
//    private final Long id;
//    private final String title;
//    private final String writer;
//    private final String createDate;
//    private final Long totalLikes;
//    private final Long views;
//
//    /* Entity -> DTO */
//    @Builder
//    public CodeQuestionListResponseDTO(Long id, String title, String writer, String createDate, Long totalLikes, Long views) {
//        this.id = id;
//        this.title = title;
//        this.writer = writer;
//        this.createDate = createDate;
//        this.totalLikes = totalLikes;
//        this.views = views;
//    }
//
//    public CodeQuestionListResponseDTO(CodeQuestion codeQuestion) {
//        this.id = codeQuestion.getId();
//        this.title = codeQuestion.getTitle();
//        this.writer = codeQuestion.getWriter();
//        this.createDate = codeQuestion.getCreateDate();
//        this.totalLikes = codeQuestion.getTotalLikes();
//        this.views = codeQuestion.getViews();
//    }
//}