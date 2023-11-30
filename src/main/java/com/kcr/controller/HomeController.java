package com.kcr.controller;

import com.kcr.domain.dto.codequestion.CodeQuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.repository.CodeQuestionCommentRepository;
import com.kcr.repository.CodeQuestionRepository;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class HomeController {

    private final QuestionRepository questionRepository;
    private final QuestionCommentRepository questionCommentRepository;
    private final CodeQuestionCommentRepository codeQuestionCommentRepository;
    private final CodeQuestionRepository codeQuestionRepository;

    @GetMapping("/question/top/five")
    public ResponseEntity<Page<QuestionListResponseDTO>> findTop5Question(Pageable pageable) {
        Page<QuestionListResponseDTO> questionListResponseDTOS = questionRepository.findTop5ByOrderByCreateDateDesc(pageable);

        questionListResponseDTOS.stream().forEach(questionListResponseDTO -> {
            questionListResponseDTO.setTotalComments(Math.toIntExact(questionCommentRepository.countByQuestionId(questionListResponseDTO.getId())));
        });

        return new ResponseEntity<>(questionListResponseDTOS, HttpStatus.OK);
    }


    @GetMapping("/codequestion/top/five")
    public ResponseEntity<Page<CodeQuestionListResponseDTO>> findTop5CodeQuestion(Pageable pageable) {
        Page<CodeQuestionListResponseDTO> codeQuestionListResponseDTOS = codeQuestionRepository.findTop5ByOrderByCreateDateDesc(pageable);

       codeQuestionListResponseDTOS.stream().forEach(codeQuestionListResponseDTO -> {
           codeQuestionListResponseDTO.setTotalComments(Math.toIntExact(codeQuestionCommentRepository.countByCodeQuestionId(codeQuestionListResponseDTO.getId())));
       });

        return new ResponseEntity<>(codeQuestionListResponseDTOS, HttpStatus.OK);
    }
}