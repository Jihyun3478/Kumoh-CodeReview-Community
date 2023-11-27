package com.kcr.controller;

import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Question;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.repository.QuestionCommentRepository;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionCommentService questionCommentService;
    private final ChatGptService chatGptService;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final QuestionCommentRepository questionCommentRepository;

    /* ================ API ================ */
    /* 게시글 등록 */
    @PostMapping("/api/question")
    public ResponseEntity<QuestionResponseDTO> save(@RequestBody QuestionRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();
        try {
            if(!validateMemberRole(roleType)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        requestDTO.setWriter(loginMember.getNickname());
        return new ResponseEntity<>(questionService.save(requestDTO), HttpStatus.OK);
    }

    @PatchMapping("/api/question/{id}")
    public ResponseEntity<QuestionResponseDTO> update(@PathVariable Long id, @RequestBody QuestionRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        String writer = questionRepository.findById(id).get().getWriter();

        try {
            if (!Objects.equals(writer, loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(questionService.update(id, requestDTO), HttpStatus.OK);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/api/question/{id}")
    public ResponseEntity<MsgResponseDTO> delete(@PathVariable Long id, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        Question question = questionRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        });

        try {
            if (!Objects.equals(question.getWriter(), loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(questionService.delete(id), HttpStatus.OK);
    }

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
    public ResponseEntity<Page<QuestionListResponseDTO>> findAllByLikes(@PageableDefault(sort = "totalLikes", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<QuestionListResponseDTO> responseDTOS = questionService.findAll(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    /* 게시글 상세 조회 + 조회수 업데이트 */
    @GetMapping("/question/{id}")
    public ResponseEntity<QuestionResponseDTO> findById(@PathVariable("id") Long id, Model model, HttpSession session,@PageableDefault(size = 5)Pageable pageable) {
        Member loginMember = (Member)session.getAttribute("loginMember");
        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();

        try {
            if (!validateMemberRole(roleType)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        QuestionResponseDTO questionResponseDTO = questionService.findById(id);
        questionService.updateViews(id); // views++
        String content = questionResponseDTO.getContent();
        log.info("질문내용: "+content);
        ChatGptResponse chatGptResponse = null;
        //gpt 댓글
        try {
             chatGptResponse = chatGptService.findById(id);
            questionResponseDTO.setChatGPT(chatGptResponse);
            log.info("gpt", questionResponseDTO);

        } catch (NullPointerException e) {
            questionResponseDTO.setChatGPT(null);
            log.error("error", questionResponseDTO);
        }

        //댓글 페이징 처리
        Long totalComments = questionCommentRepository.countByQuestionId(id);
        Page<QuestionCommentResponseDTO> questionCommentDTOPage = questionCommentService.findAllWithChild2(id, pageable);
        questionResponseDTO.setQuestionComments(questionCommentDTOPage);
        questionResponseDTO.setTotalComments(totalComments);

        model.addAttribute("questionCommentList", questionCommentDTOPage);
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

    private boolean validateMemberRole(RoleType roleType) {
        return (roleType == RoleType.USER) || (roleType == RoleType.ADMIN);
    }
}
