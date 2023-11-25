package com.kcr.controller;

//import com.kcr.domain.dto.member.security.MyUserDetails;
import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.member.security.MyUserDetails;
import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Member;
import com.kcr.repository.QuestionRepository;
import com.kcr.service.ChatGptService;
import com.kcr.service.QuestionCommentService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final QuestionCommentService questionCommentService;
    private final ChatGptService chatGptService;

    @GetMapping("/api/all")
    public ResponseEntity<String> test(@AuthenticationPrincipal MyUserDetails myUserDetails) {

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return ResponseEntity.ok("success");

    }

    /* ================ API ================ */
    /* 게시글 등록 */
    @PostMapping("/api/question")
    public ResponseEntity<QuestionResponseDTO> save(@RequestBody QuestionRequestDTO requestDTO, @AuthenticationPrincipal MyUserDetails myUserDetails) { //
        return ResponseEntity.ok(questionService.save(requestDTO, myUserDetails.getMember())); //
//        return questionService.save(requestDTO);
    }

    /* 게시글 수정 */
    @PatchMapping("/api/question/{id}")
    public ResponseEntity<QuestionResponseDTO> update(@PathVariable Long id, @RequestBody QuestionRequestDTO requestDTO, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return ResponseEntity.ok(questionService.update(id, requestDTO, myUserDetails.getMember()));
    }

    /* 게시글 삭제 */
    @DeleteMapping("/api/question/{id}")
    public ResponseEntity<MsgResponseDTO> delete(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return ResponseEntity.ok(questionService.delete(id, myUserDetails.getMember()));
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
                .writer(responseDTO.getWriter())
                .content(responseDTO.getContent())
                .createDate(responseDTO.getCreateDate())
                .totalLikes(responseDTO.getTotalLikes())
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
    public ResponseEntity<QuestionResponseDTO> findById(@PathVariable("id") Long id, Model model) {
        QuestionResponseDTO questionResponseDTO = questionService.findById(id);
        questionService.updateViews(id); // views++
        String content = questionResponseDTO.getContent();
        log.info("질문내용: "+content);

        //gpt 댓글
        ChatGptResponse chatGptResponse = chatGptService.findById(id);
        questionResponseDTO.setChatGPT(chatGptResponse);

        //댓글 페이징 처리
        List<QuestionCommentResponseDTO> questionCommentDTOList = questionCommentService.findAllWithChild2(id, 1);
        questionResponseDTO.setQuestionComments(questionCommentDTOList);

        System.out.println("댓글 사이즈는 : "+ questionCommentDTOList.size());
        for(int i = 0;i<questionCommentDTOList.size();i++){
            System.out.println("questionList : "+questionCommentDTOList.get(i).getQuestion_id());
        }

        model.addAttribute("questionCommentList", questionCommentDTOList);
        model.addAttribute("chatGPT", chatGptResponse);

        return new ResponseEntity<>(questionResponseDTO, HttpStatus.OK);
    }

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
