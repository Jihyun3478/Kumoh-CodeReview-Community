package com.kcr.domain.dto.questioncomment;

import com.kcr.domain.entity.QuestionComment;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Getter
@Setter
public class QuestionCommentResponseDTO {
    private Long question_comment_id;
    private String content;
    private Long likes;
    private String writer;
    private Long question_id;
    private List<QuestionCommentResponseDTO> childComments;
    @Builder
    public QuestionCommentResponseDTO(Long question_comment_id, String content, Long likes, String writer,Long question_id,List<QuestionCommentResponseDTO> childComments) {
        this.question_comment_id = question_comment_id;
        this.content = content;
        this.likes = likes;
        this.writer = writer;
        this.question_id= question_id;
        this.childComments = childComments;
    }
    @Builder
    public QuestionCommentResponseDTO(QuestionComment questionComment) {
        this.question_comment_id=questionComment.getId();
        this.content=questionComment.getContent();
        this.likes=questionComment.getLikes();
        this.writer=questionComment.getWriter();
        this.question_id=questionComment.getQuestion().getId();
    }
    /*public static QuestionCommentResponseDTO toCommentDTO(QuestionComment questionComment, Long questionId) {
        QuestionCommentResponseDTO questionCommentResponseDTO = new QuestionCommentResponseDTO();
        questionCommentResponseDTO.setQuestion_comment_id(questionComment.getId());
        questionCommentResponseDTO.setContent(questionComment.getContent());
        questionCommentResponseDTO.setLikes(questionComment.getLikes());
        questionCommentResponseDTO.setWriter(questionComment.getWriter());
        questionCommentResponseDTO.setQuestion_id(questionId);
        // 대댓글 목록을 DTO로 변환
        if (questionComment.getChild() != null) {
            List<QuestionCommentResponseDTO> commentsDTO = questionComment.getChild().stream()
                    .map(childComments -> toCommentDTO(childComments))
                    .collect(Collectors.toList());
            questionCommentResponseDTO.setChildComments(commentsDTO);
        }
        return questionCommentResponseDTO;
    }*/

    public static QuestionCommentResponseDTO toCommentDTO2(QuestionComment questionComment) {
        List<QuestionCommentResponseDTO> childCommentsDTO = null;
        if (questionComment.getChild() != null) {
            childCommentsDTO = questionComment.getChild().stream()
                    .map(childComment -> toCommentDTO2(childComment))
                    .collect(Collectors.toList());
        }

        return QuestionCommentResponseDTO.builder()
                .question_comment_id(questionComment.getId())
                .content(questionComment.getContent())
                .likes(questionComment.getLikes())
                .writer(questionComment.getWriter())
                .question_id(questionComment.getQuestion().getId())
                .childComments(childCommentsDTO)
                .build();
    }


}
