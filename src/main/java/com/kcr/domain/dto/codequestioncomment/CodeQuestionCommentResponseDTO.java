package com.kcr.domain.dto.codequestioncomment;

import com.kcr.domain.entity.CodeQuestionComment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CodeQuestionCommentResponseDTO {
    private Long code_question_comment_id;
    private String content;
    private String codeContent;
    private Long totalLikes;
    private String writer;
    private Long codeQuestion_id;
    private String createDate;
    private List<CodeQuestionCommentResponseDTO> childComments;

    //Builder가 2개 있으면 위의 Builder 부터 읽음
    @Builder
    public CodeQuestionCommentResponseDTO(Long code_question_comment_id, String content, String codeContent, Long totalLikes, String writer, Long codeQuestionId, List<CodeQuestionCommentResponseDTO> child) {
        this.code_question_comment_id=code_question_comment_id;
        this.content=content;
        this.totalLikes=totalLikes;
        this.codeContent = codeContent;
        this.writer=writer;
        this.codeQuestion_id=codeQuestionId;
        this.childComments=child;
    }
    @Builder
    public CodeQuestionCommentResponseDTO(CodeQuestionComment codeQuestionComment) {
        this.code_question_comment_id=codeQuestionComment.getId();
        this.content=codeQuestionComment.getContent();
        this.totalLikes=codeQuestionComment.getTotalLikes();
        this.writer=codeQuestionComment.getWriter();
        this.createDate = codeQuestionComment.getCreateDate();
        this.codeQuestion_id=codeQuestionComment.getCodeQuestion().getId();
    }

    public static CodeQuestionCommentResponseDTO toCommentDTO(CodeQuestionComment codeQuestionComment) {
        // 자식 댓글 리스트 초기화
        List<CodeQuestionCommentResponseDTO> childCommentsDTO = new ArrayList<>();

        // 자식 댓글 처리
        if (codeQuestionComment.getChild() != null) {
            for (CodeQuestionComment childComment : codeQuestionComment.getChild()) {
                // 부모 댓글의 ID와 다른 경우에만 자식 댓글 목록에 추가
                if (!childComment.getId().equals(codeQuestionComment.getId())) {
                    childCommentsDTO.add(toCommentDTO(childComment));
                }
            }
        }

        // DTO 생성
        return CodeQuestionCommentResponseDTO.builder()
                .code_question_comment_id(codeQuestionComment.getId())
                .content(codeQuestionComment.getContent())
                .codeContent(codeQuestionComment.getCodeContent())
                .totalLikes(codeQuestionComment.getTotalLikes())
                .writer(codeQuestionComment.getWriter())
                .codeQuestionId(codeQuestionComment.getCodeQuestion().getId())
                .child(childCommentsDTO)
                .build();
    }
}
