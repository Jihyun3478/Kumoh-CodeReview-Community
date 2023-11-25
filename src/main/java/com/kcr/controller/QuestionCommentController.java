package com.kcr.controller;

import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.service.QuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionCommentController {
    @Autowired
    private QuestionCommentService questionCommentService;

    //댓글 수정 , 부분수정이여서 PatchMapping 으로 바꿈
    @PatchMapping("/question/{questionId}/comments/{id}")
    public ResponseEntity<Long> update(@PathVariable Long questionId,
                                       @PathVariable Long id,
                                       @RequestBody QuestionCommentRequestDTO requestDTO) {
        questionCommentService.updateComment(questionId, id, requestDTO);
        return ResponseEntity.ok(id);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}/delete")
    public void delete(@PathVariable("id") Long id) {
        questionCommentService.delete(id);
    }


    //댓글등록
    @PostMapping("/question/{id}/comment")
    public Long commentSave(@PathVariable Long id, @RequestBody QuestionCommentRequestDTO questionCommentRequestDTO) {
        Long commentID = questionCommentService.commentSave(id, questionCommentRequestDTO);
        //댓글알림
       // pushService.notifyLikesByComment(id);
        System.out.println("questionCommentID입니다 :"+commentID);
        return commentID;
    }
    //대댓글 등록
    @PostMapping("/question/{id}/comment/{parentId}/child")
    public ResponseEntity<String> createChildComment(@PathVariable Long parentId,@PathVariable Long id,
                                                   @RequestBody QuestionCommentRequestDTO requestDTO) {
            try {
                Long childCommentId = questionCommentService.saveChildComment(parentId, id, requestDTO);
                String childCommentIDtoString = String.valueOf(childCommentId);
                return ResponseEntity.ok().body(childCommentIDtoString);
            }catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    // 대댓글만 조회(테스트용)
    @GetMapping("/question/{id}/comment/{commentId}/child")
    public ResponseEntity<List<QuestionCommentResponseDTO>> getChildComments(@PathVariable Long commentId) {
        List<QuestionCommentResponseDTO> childComments = questionCommentService.findAllChildComments(commentId);
        return ResponseEntity.ok(childComments);
    }

}