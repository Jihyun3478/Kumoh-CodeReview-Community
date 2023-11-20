package com.kcr.controller.api;

import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.entity.QuestionComment;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.service.QuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestionCommentApiController {
    private QuestionCommentService questionCommentService;
    private final QuestionCommentRepository questionCommentRepository;

    //댓글등록
    @PostMapping("/questioncomment/create") //kcr/question
    public Long postSave(@RequestBody QuestionCommentRequestDTO questionCommentRequestDTO) {
        return questionCommentService.save(questionCommentRequestDTO);
    }
    //댓글 수정
    @Transactional
    @PutMapping("/questioncomment/update/{id}")
    public QuestionCommentRequestDTO update(@PathVariable Long id, @RequestBody QuestionCommentRequestDTO requestQuestionCommentDTO ) {
        System.out.println("id: "+id);
        System.out.println("바뀐내용입니다 : "+requestQuestionCommentDTO.getContent());
        questionCommentService.update(id, requestQuestionCommentDTO);
        return requestQuestionCommentDTO;
    }

    // 댓글 삭제
    @DeleteMapping("/questioncomment/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        questionCommentService.delete(id);
    }

    //댓글 좋아요
    @PutMapping("/questioncomment/likes/{id}")
    public void likeComment(@PathVariable("id") Long id) {
        questionCommentService.updateLikes(id);
    }
    //댓글 좋아요 취소
    @PutMapping("/questioncomment/cancellikes/{id}")
    public void likeCancel(@PathVariable("id") Long id) {
        questionCommentService.cancelLikes(id);
    }
    @PostConstruct
    public void init() {
        for(int i = 1; i <= 100; i++) {
            questionCommentRepository.save(new QuestionComment("content" + i, 0L, "writer" + i));
        }
    }
}