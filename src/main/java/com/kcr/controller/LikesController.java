package com.kcr.controller;

import com.kcr.domain.dto.likes.LikesRequestDTO;
import com.kcr.domain.dto.likes.LikesResponseDTO;
import com.kcr.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    //좋아요 전체 출력(q&a)
    @GetMapping("/question/{id}/likes")
    public ResponseEntity<List<LikesResponseDTO>> findAllLikes(@PathVariable Long id) {
        List<LikesResponseDTO> likeList = likesService.findAllByQuestion(id);
        return new ResponseEntity<>(likeList,HttpStatus.OK);
    }

    //게시글 좋아요 등록(q&a)
    @PostMapping("/question/{id}/likes")
    public Long questionLikes(@PathVariable  Long id) {
        LikesRequestDTO likesRequestDTO = new LikesRequestDTO();
        likesService.questionLikes(id, likesRequestDTO);
        return id;
    }

    //게시글 좋아요 취소(q&a)
    @DeleteMapping("/question/{id}/likescancel")
    public Long cancleQuestionLikes(@PathVariable  Long id) {
        likesService.cancelQuestionLikes(id);
        return id;
    }

    //게시글 댓글 좋아요 등록(q&a)
    @PostMapping("/question/{id}/comment/{commendId}/likes")
    public Long questionCommentLikes(@PathVariable  Long commendId) {
        LikesRequestDTO likesRequestDTO = new LikesRequestDTO();
        likesService.questionCommentLikes(commendId, likesRequestDTO);
        return commendId;
    }

    //게시글 댓글 좋아요 취소(q&a)
    @DeleteMapping("/question/{id}/comment/{commendId}/likescancel")
    public Long cancelQuestionCommentLikes(@PathVariable  Long commendId) {
        likesService.cancelquestionCommentLikes(commendId);
        return commendId;
    }


    //게시글 좋아요 등록(codequestion)
    @PostMapping("/codequestion/{id}/likes")
    public Long codeQuestionLikes(@PathVariable  Long id) {
        LikesRequestDTO likesRequestDTO = new LikesRequestDTO();
        likesService.codeQuestionLikes(id, likesRequestDTO);
        return id;
    }

    //게시글 좋아요 취소(codequestion)
    @DeleteMapping("/codequestion/{id}/likescancel")
    public Long cancleCodeQuestionLikes(@PathVariable  Long id) {
        likesService.cancelCodeQuestionLikes(id);
        return id;
    }

    //게시글 댓글 좋아요 등록(codequestion)
    @PostMapping("/codequestion/{id}/comment/{commendId}/likes")
    public Long codeQuestionCommentLikes(@PathVariable  Long commendId) {
        LikesRequestDTO likesRequestDTO = new LikesRequestDTO();
        likesService.codeQuestionCommentLikes(commendId,likesRequestDTO);
        return commendId;
    }

    //게시글 댓글 좋아요 취소(codequestion)
    @DeleteMapping("/codequestion/{id}/comment/{commendId}/likescancel")
    public Long cancelCodeQuestionCommentLikes(@PathVariable  Long commendId) {
        likesService.cancelCodeQuestionCommentLikes(commendId);
        return commendId;
    }
}
