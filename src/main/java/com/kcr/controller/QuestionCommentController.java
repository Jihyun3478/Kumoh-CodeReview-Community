package com.kcr.controller;

import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.service.QuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionCommentController {
    private QuestionCommentService questionCommentService;
    private final QuestionCommentRepository questionCommentRepository;

    //공감순 댓글 조회
    @GetMapping("/kcr/questioncomment")
    public Page<QuestionCommentResponseDTO> findAllByCreateDate(@PageableDefault(sort = "likes", direction = Sort.Direction.DESC, size = 6) Pageable pageable) {
        return questionCommentRepository.findAll(pageable)
                .map(QuestionCommentResponseDTO::new);
    }

}
