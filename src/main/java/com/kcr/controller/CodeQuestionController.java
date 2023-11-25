package com.kcr.controller;

import com.kcr.domain.dto.codequestion.CodeQuestionRequestDTO;
import com.kcr.domain.dto.codequestion.CodeQuestionResponseDTO;
import com.kcr.domain.dto.codequestion.CodeQuestionListResponseDTO;
import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.member.security.MyUserDetails;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import com.kcr.repository.CodeQuestionRepository;
import com.kcr.service.CodeQuestionCommentService;
import com.kcr.service.CodeQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CodeQuestionController {

    private final CodeQuestionService codeQuestionService;
    private final CodeQuestionRepository codeQuestionRepository;
    private final CodeQuestionCommentService codeQuestionCommentService;

    /* ================ API ================ */
    /* 게시글 등록 */
    @PostMapping("/api/codequestion")
    public ResponseEntity<CodeQuestionResponseDTO> save(@RequestBody CodeQuestionRequestDTO requestDTO, @AuthenticationPrincipal(errorOnInvalidType=true) MyUserDetails myUserDetails) { //
        return ResponseEntity.ok(codeQuestionService.save(requestDTO, myUserDetails.getMember()));
//        return questionService.save(requestDTO);
    }

    /* 게시글 수정 */
    @PatchMapping("/api/codequestion/{id}")
    public ResponseEntity<CodeQuestionResponseDTO> update(@PathVariable Long id, @RequestBody CodeQuestionRequestDTO requestDTO, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return ResponseEntity.ok(codeQuestionService.update(id, requestDTO, myUserDetails.getMember()));
    }

    /* 게시글 삭제 */
    @DeleteMapping("/api/codequestion/{id}")
    public ResponseEntity<MsgResponseDTO> delete(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return ResponseEntity.ok(codeQuestionService.delete(id, myUserDetails.getMember()));
    }

    // 테스트 데이터
//    @PostConstruct
//    public void init() {
//        for(int i = 1; i <= 100; i++) {
//            codeQuestionRepository.save(new CodeQuestion("title" + i, "writer" + i, "content" + i, "code" + i, 100L + i, 100L));
//        }
//    }


    /* ================ UI ================ */
    /* 게시글 수정 화면 */
    @GetMapping("/codequestion/{id}/edit")
    public ResponseEntity<CodeQuestionResponseDTO> updateCodeQuestion(@PathVariable("id") Long id) {
        CodeQuestionResponseDTO responseDTO = codeQuestionService.findById(id);

        CodeQuestionResponseDTO.builder()
                .id(responseDTO.getId())
                .title(responseDTO.getTitle())
                .writer(responseDTO.getWriter())
                .content(responseDTO.getContent())
                .codeContent(responseDTO.getCodeContent())
                .createDate(responseDTO.getCreateDate())
                .totalLikes(responseDTO.getTotalLikes())
                .views(responseDTO.getViews())
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /* 게시글 전체 조회 화면 + 최신순 정렬*/
    @GetMapping("/codequestion")
    public ResponseEntity<Page<CodeQuestionListResponseDTO>> findAllByCreateDate(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.findAll(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    /* 게시글 전체 조회 화면 + 공감순 정렬 */
    @GetMapping("/codequestionByLikes")
    public Page<CodeQuestionListResponseDTO> findAllByLikes(@PageableDefault(sort = "likes", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        return codeQuestionRepository.findAll(pageable)
                .map(CodeQuestionListResponseDTO::new);
    }

    /* 게시글 상세 조회 + 조회수 업데이트 */
    @GetMapping("/codequestion/{id}")
    public ResponseEntity<CodeQuestionResponseDTO> findById(@PathVariable("id") Long id, Model model) {
        CodeQuestionResponseDTO responseDTO = codeQuestionService.findById(id);
        codeQuestionService.updateViews(id); // views++

        //댓글
        List<CodeQuestionCommentResponseDTO> codeQuestionCommentResponseDTOList = codeQuestionCommentService.findAllWithChild2(id, 1);
        responseDTO.setCodeQuestionComment(codeQuestionCommentResponseDTOList);

        model.addAttribute("codeQuestionCommentList", codeQuestionCommentResponseDTOList);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    /* 좋아요 업데이트 */

    /* 게시글 제목 검색 */
    @GetMapping("/codequestion/search")
    public ResponseEntity<Page<CodeQuestionListResponseDTO>> searchByTitle(String title, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.searchByTitle(title, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    /* 게시글 작성자 검색 */
    @GetMapping("/codequestion/searchByWriter")
    public ResponseEntity<Page<CodeQuestionListResponseDTO>> searchByWriter(String writer, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.searchByWriter(writer, pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }
}
