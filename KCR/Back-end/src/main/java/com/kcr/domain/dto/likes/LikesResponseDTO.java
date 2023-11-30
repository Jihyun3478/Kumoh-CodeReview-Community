package com.kcr.domain.dto.likes;

import com.kcr.domain.entity.Likes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LikesResponseDTO {
    private Long likes_id;
    private Long question_id;
    private Long question_comment_id;
    private Long code_quesetion_id;
    private Long code_question_comment_id;

    /* Entity -> DTO */
    @Builder
    public LikesResponseDTO(Long id, Long question_id, Long question_comment_id, Long code_quesetion_id, Long code_question_comment_id) {
        this.likes_id=id;
        this.question_id=question_id;
        this.question_comment_id=question_comment_id;
        this.code_quesetion_id=code_quesetion_id;
        this.code_question_comment_id=code_question_comment_id;
    }
    public LikesResponseDTO(Likes likes) {
        this.likes_id=likes.getId();
        this.question_id=likes.getQuestion().getId();
        this.question_comment_id=likes.getQuestionComment().getId();
        this.code_quesetion_id=likes.getCodeQuestion().getId();
        this.code_question_comment_id=likes.getCodeQuestionComment().getId();
    }


    public static LikesResponseDTO toLikesDTO(Likes likes) {
        LikesResponseDTO dto = new LikesResponseDTO();
        dto.setLikes_id(likes.getId());
        dto.setQuestion_id(likes.getQuestion().getId());
        dto.setQuestion_comment_id(likes.getQuestionComment().getId());
        dto.setCode_quesetion_id(likes.getCodeQuestion().getId());
        dto.setCode_question_comment_id(likes.getQuestionComment().getId());
        return dto;
    }

}
