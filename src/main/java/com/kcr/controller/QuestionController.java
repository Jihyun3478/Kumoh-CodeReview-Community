package com.kcr.controller;

import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.dto.member.security.MyUserDetails;
import com.kcr.repository.QuestionRepository;
import com.kcr.service.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private Authentication authentication;

    /* ================ API ================ */
    /* 게시글 등록 */
//    @PostMapping("/api/question")
//    public Long save(@RequestBody QuestionRequestDTO requestDTO, @AuthenticationPrincipal MyUserDetails myUserDetails) {
//        return questionService.save(requestDTO, myUserDetails.getMember());
//        requestDTO.setWriter(loginDTO.getLoginId());
//        return questionService.save(requestDTO);
//    }

    /* 게시글 수정 */
    @PatchMapping("/api/question/{id}")
    public Long update(@PathVariable Long id, @RequestBody QuestionRequestDTO requestDTO) {
        return questionService.update(id, requestDTO);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/api/question/{id}")
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }

    // 테스트 데이터
//    @PostConstruct
//    public void init() {
//        for(int i = 1; i <= 100; i++) {
//            questionRepository.save(new Question("title" + i, "writer" + i, "content" + i, 100L + i, 100L));
//        }
//    }

    /* ================ UI ================ */
    /* 게시글 수정 화면 */
    @GetMapping("/question/{id}/edit")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(@PathVariable("id") Long id) {
        QuestionResponseDTO responseDTO = questionService.findById(id);

         QuestionResponseDTO.builder()
                .id(responseDTO.getId())
                .title(responseDTO.getTitle())
                .content(responseDTO.getContent())
                .createDate(responseDTO.getCreateDate())
                .likes(responseDTO.getLikes())
                .views(responseDTO.getViews())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /* 게시글 전체 조회 화면 + 최신순 정렬*/
    @GetMapping("/question")
    public ResponseEntity<Page<QuestionListResponseDTO>> findAllByCreateDate(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<QuestionListResponseDTO> responseDTOS = questionService.findAll(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    /* 게시글 전체 조회 화면 + 공감순 정렬 */
    @GetMapping("/questionByLikes")
    public Page<QuestionListResponseDTO> findAllByLikes(@PageableDefault(sort = "likes", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        return questionRepository.findAll(pageable)
                .map(QuestionListResponseDTO::new);
    }

    /* 게시글 상세 조회 + 조회수 업데이트 */
    @GetMapping("/question/{id}")
    public ResponseEntity<QuestionResponseDTO> findById(@PathVariable("id") Long id) {
        QuestionResponseDTO questionResponseDTO = questionService.findById(id);
        questionService.updateViews(id); // views++
        return new ResponseEntity<>(questionResponseDTO, HttpStatus.OK);
    }

    /* 좋아요 업데이트 */


    /* 게시글 제목 검색 */
    @GetMapping("/question/search")
    public ResponseEntity<Page<QuestionListResponseDTO>> searchByTitle(String title, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<QuestionListResponseDTO> responseDTOS = questionService.searchByTitle(title, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    /* 게시글 작성자 검색 */
    @GetMapping("/question/searchByWriter")
    public ResponseEntity<Page<QuestionListResponseDTO>> searchByWriter(String writer, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<QuestionListResponseDTO> responseDTOS = questionService.searchByWriter(writer, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }
}
