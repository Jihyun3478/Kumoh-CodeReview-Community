package com.kcr.domain.dto.questioncomment;

import com.kcr.domain.entity.QuestionComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class QuestionCommentResponseDTO {
    private Long question_comment_id;
    private String content;
    private Long totalLikes;
    private String writer;
    private Long question_id;
    private String createDate;
    private List<QuestionCommentResponseDTO> childComments;
    @Builder
    public QuestionCommentResponseDTO(Long question_comment_id, String content, Long totalLikes, String writer,Long question_id,List<QuestionCommentResponseDTO> childComments) {
        this.question_comment_id = question_comment_id;
        this.content = content;
        this.totalLikes = totalLikes;
        this.writer = writer;
        this.question_id= question_id;
        this.childComments = childComments;
    }
    @Builder
    public QuestionCommentResponseDTO(QuestionComment questionComment) {
        this.question_comment_id=questionComment.getId();
        this.content=questionComment.getContent();
        this.totalLikes=questionComment.getTotalLikes();
        this.writer=questionComment.getWriter();
        this.createDate = questionComment.getCreateDate();
        this.question_id=questionComment.getQuestion().getId();
    }

    public static QuestionCommentResponseDTO toCommentDTO(QuestionComment questionComment) {
        // 자식 댓글 리스트 초기화
        List<QuestionCommentResponseDTO> childCommentsDTO = new ArrayList<>();

        // 자식 댓글 처리
        if (questionComment.getChild() != null) {

            for (QuestionComment childComment : questionComment.getChild()) {
                if (!childComment.getId().equals(questionComment.getId())) {
                    childCommentsDTO.add(toCommentDTO(childComment));
                }
            }
        }

        // DTO 생성
        return QuestionCommentResponseDTO.builder()
                .question_comment_id(questionComment.getId())
                .content(questionComment.getContent())
                .totalLikes(questionComment.getTotalLikes())
                .writer(questionComment.getWriter())
                .question_id(questionComment.getQuestion().getId())
                .childComments(childCommentsDTO)
                .build();
    }
}