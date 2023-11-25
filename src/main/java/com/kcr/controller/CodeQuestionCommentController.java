package com.kcr.controller;

import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentRequestDTO;
import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.service.CodeQuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CodeQuestionCommentController {
    @Autowired
    private CodeQuestionCommentService codeQuestionCommentService;
    //댓글 수정 , 부분수정이여서 PatchMapping 으로 바꿈
    @PatchMapping("/codequestion/{codequestionid}/comments/{id}")
    public ResponseEntity<Long> update(@PathVariable Long codequestionid,
                                       @PathVariable Long id,
                                       @RequestBody CodeQuestionCommentRequestDTO requestDTO) {
        codeQuestionCommentService.updateComment(codequestionid, id, requestDTO);
        return ResponseEntity.ok(id);
    }

    // 댓글 삭제
    @DeleteMapping("/codequestion/{codequestionid}/comments/{id}/delete")
    public void delete(@PathVariable("id") Long id) {
        codeQuestionCommentService.delete(id);
    }


    //댓글등록(일반 댓글과 코드 관련댓글 공백일시 예외처리)
    @PostMapping("/codequestion/{id}/codecomment")
    public ResponseEntity<String> commentSave(@PathVariable Long id, @RequestBody CodeQuestionCommentRequestDTO codeQuestionCommentRequestDTO) {

        System.out.println("====================start====================================");
        try {
            Long commentId = codeQuestionCommentService.commentSave(id, codeQuestionCommentRequestDTO);
            System.out.println("commentID :"+commentId);
            String commentIDtoString = String.valueOf(commentId);

            System.out.println("commentIDtoString :"+commentIDtoString);
            return ResponseEntity.ok().body(commentIDtoString);
        } catch (IllegalArgumentException e) {
            System.out.println("error :");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //대댓글 등록
    @PostMapping("/codequestion/{id}/comment/{parentId}/child")
    public ResponseEntity<String> createChildComment(@PathVariable Long parentId,@PathVariable Long id,
                                                   @RequestBody CodeQuestionCommentRequestDTO requestDTO) {
        try {
            Long childCommentId = codeQuestionCommentService.saveChildComment(parentId, id, requestDTO);
            String childCommentIDtoString = String.valueOf(childCommentId);
            return ResponseEntity.ok().body(childCommentIDtoString);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 대댓글만 조회(테스트용)
    @GetMapping("/codequestion/{id}/comment/{commentId}/children")
    public ResponseEntity<List<CodeQuestionCommentResponseDTO>> getChildComments(@PathVariable Long commentId) {
        List<CodeQuestionCommentResponseDTO> childComments = codeQuestionCommentService.findAllChildComments(commentId);
        return ResponseEntity.ok(childComments);
    }

}
