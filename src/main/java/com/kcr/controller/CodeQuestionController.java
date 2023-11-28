package com.kcr.controller;

import com.kcr.domain.dto.codequestion.CodeQuestionListResponseDTO;
import com.kcr.domain.dto.codequestion.CodeQuestionRequestDTO;
import com.kcr.domain.dto.codequestion.CodeQuestionResponseDTO;
import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.CodeQuestionCommentRepository;
import com.kcr.repository.CodeQuestionRepository;
import com.kcr.repository.MemberRepository;
import com.kcr.service.ChatGptService;
import com.kcr.service.CodeQuestionCommentService;
import com.kcr.service.CodeQuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@Slf4j
@RequiredArgsConstructor
public class CodeQuestionController {

    private final CodeQuestionService codeQuestionService;
    private final CodeQuestionCommentService codeQuestionCommentService;
    private final MemberRepository memberRepository;
    private final CodeQuestionRepository codeQuestionRepository;
    private final CodeQuestionCommentRepository codeQuestionCommentRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /* ================ API ================ */
    /* 게시글 등록 */ // 세션 아이디를 뽑아서 바디에 넣는다
    @PostMapping("/api/codequestion")
    public ResponseEntity<CodeQuestionResponseDTO> save(@RequestBody CodeQuestionRequestDTO requestDTO, HttpSession session) { // , HttpSession session
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
        return new ResponseEntity<>(codeQuestionService.save(requestDTO), HttpStatus.OK);
    }

    /* 게시글 수정 */
//    @PatchMapping("/api/codequestion/{id}")
//    public ResponseEntity<CodeQuestionResponseDTO> update(@PathVariable Long id, @RequestBody CodeQuestionRequestDTO requestDTO, HttpSession session) {
//        Member loginMember = (Member)session.getAttribute("loginMember");
//
//        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
//        if(!Objects.equals(requestDTO.getWriter(), loginNickname.getNickname())) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        return new ResponseEntity<>(codeQuestionService.update(id, requestDTO), HttpStatus.OK);
//    }

    @PatchMapping("/api/codequestion/{id}")
    public ResponseEntity<CodeQuestionResponseDTO> update(@PathVariable Long id, @RequestBody CodeQuestionRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        String writer = codeQuestionRepository.findById(id).get().getWriter();

        System.out.println("loginNickname = " + loginNickname.getNickname());
        try {
            if (!Objects.equals(writer, loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        return new ResponseEntity<>(codeQuestionService.update(id, requestDTO), HttpStatus.OK);
    }

    /* 게시글 삭제 */
    @DeleteMapping("/api/codequestion/{id}")
    public ResponseEntity<MsgResponseDTO> delete(@PathVariable Long id, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        CodeQuestion codeQuestion = codeQuestionRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        });

        try {
            if (!Objects.equals(codeQuestion.getWriter(), loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(codeQuestionService.delete(id), HttpStatus.OK);
    }

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
    public ResponseEntity<Page<CodeQuestionListResponseDTO>> findAllByLikes(@PageableDefault(sort = "totalLikes", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.findAll(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    /* 게시글 상세 조회 + 조회수 업데이트 */
    @GetMapping("/codequestion/{id}")
    public ResponseEntity<CodeQuestionResponseDTO> findById(@PathVariable("id") Long id, Model model, HttpSession session,@PageableDefault(size = 5) Pageable pageable) {
        Member loginMember = (Member)session.getAttribute("loginMember");
        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();

        try {
            if (!validateMemberRole(roleType)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        CodeQuestionResponseDTO responseDTO = codeQuestionService.findById(id);
        codeQuestionService.updateViews(id); // views++

        //댓글
        Long totalComments = codeQuestionCommentRepository.countByCodeQuestionId(id);
        Page<CodeQuestionCommentResponseDTO> codeQuestionCommentDTOPage = codeQuestionCommentService.findAllWithChild2(id, pageable);
        responseDTO.setCodeQuestionComment(codeQuestionCommentDTOPage);
        responseDTO.setTotalComments(totalComments);

        model.addAttribute("codeQuestionCommentList", codeQuestionCommentDTOPage);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

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

    private boolean validateMemberRole(RoleType roleType) {
        return (roleType == RoleType.USER) || (roleType == RoleType.ADMIN);
    }
}

//package com.kcr.controller;
//
//import com.kcr.domain.dto.codequestion.CodeQuestionListResponseDTO;
//import com.kcr.domain.dto.codequestion.CodeQuestionRequestDTO;
//import com.kcr.domain.dto.codequestion.CodeQuestionResponseDTO;
//import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
//import com.kcr.domain.dto.member.MsgResponseDTO;
//import com.kcr.domain.entity.CodeQuestion;
//import com.kcr.domain.entity.Member;
//import com.kcr.domain.type.RoleType;
//import com.kcr.repository.CodeQuestionCommentRepository;
//import com.kcr.repository.CodeQuestionRepository;
//import com.kcr.repository.MemberRepository;
//import com.kcr.service.CodeQuestionCommentService;
//import com.kcr.service.CodeQuestionService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpSession;
//import java.util.Objects;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//public class CodeQuestionController {
//
//    private final CodeQuestionService codeQuestionService;
//    private final CodeQuestionCommentService codeQuestionCommentService;
//    private final MemberRepository memberRepository;
//    private final CodeQuestionRepository codeQuestionRepository;
//    private final CodeQuestionCommentRepository codeQuestionCommentRepository;
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /* ================ API ================ */
//    /* 게시글 등록 */ // 세션 아이디를 뽑아서 바디에 넣는다
//    @PostMapping("/api/codequestion")
//    public ResponseEntity<CodeQuestionResponseDTO> save(@RequestBody CodeQuestionRequestDTO requestDTO, HttpSession session) { // , HttpSession session
//        Member loginMember = (Member)session.getAttribute("loginMember");
//
//        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();
//        try {
//            if(!validateMemberRole(roleType)) {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        requestDTO.setWriter(loginMember.getNickname());
//        return new ResponseEntity<>(codeQuestionService.save(requestDTO), HttpStatus.OK);
//    }
//
//    @PatchMapping("/api/codequestion/{id}")
//    public ResponseEntity<CodeQuestionResponseDTO> update(@PathVariable Long id, @RequestBody CodeQuestionRequestDTO requestDTO, HttpSession session) {
//        Member loginMember = (Member) session.getAttribute("loginMember");
//
//        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
//        String writer = codeQuestionRepository.findById(id).get().getWriter();
//
//        System.out.println("loginNickname = " + loginNickname.getNickname());
//        try {
//            if (!Objects.equals(writer, loginNickname.getNickname())) {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<>(codeQuestionService.update(id, requestDTO), HttpStatus.OK);
//    }
//
//    /* 게시글 삭제 */
//    @DeleteMapping("/api/codequestion/{id}")
//    public ResponseEntity<MsgResponseDTO> delete(@PathVariable Long id, HttpSession session) {
//        Member loginMember = (Member)session.getAttribute("loginMember");
//
//        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
//        CodeQuestion codeQuestion = codeQuestionRepository.findById(id).orElseThrow(() -> {
//            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
//        });
//
//        try {
//            if (!Objects.equals(codeQuestion.getWriter(), loginNickname.getNickname())) {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<>(codeQuestionService.delete(id), HttpStatus.OK);
//    }
//
//    /* ================ UI ================ */
//    /* 게시글 수정 화면 */
//    @GetMapping("/codequestion/{id}/edit")
//    public ResponseEntity<CodeQuestionResponseDTO> updateCodeQuestion(@PathVariable("id") Long id) {
//        CodeQuestionResponseDTO responseDTO = codeQuestionService.findById(id);
//
//        CodeQuestionResponseDTO.builder()
//                .id(responseDTO.getId())
//                .title(responseDTO.getTitle())
//                .writer(responseDTO.getWriter())
//                .content(responseDTO.getContent())
//                .codeContent(responseDTO.getCodeContent())
//                .createDate(responseDTO.getCreateDate())
//                .totalLikes(responseDTO.getTotalLikes())
//                .views(responseDTO.getViews())
//                .build();
//
//        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//    }
//
//    /* 게시글 전체 조회 화면 + 최신순 정렬*/
//    @GetMapping("/codequestion")
//    public ResponseEntity<Page<CodeQuestionListResponseDTO>> findAllByCreateDate(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
//        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.findAll(pageable);
//        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
//    }
//
//    /* 게시글 전체 조회 화면 + 공감순 정렬 */
//    @GetMapping("/codequestionByLikes")
//    public ResponseEntity<Page<CodeQuestionListResponseDTO>> findAllByLikes(@PageableDefault(sort = "totalLikes", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
//        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.findAll(pageable);
//        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
//    }
//
//    /* 게시글 상세 조회 + 조회수 업데이트 */
//    @GetMapping("/codequestion/{id}")
//    public ResponseEntity<CodeQuestionResponseDTO> findById(@PathVariable("id") Long id, Model model, HttpSession session,@PageableDefault(size = 5) Pageable pageable) {
//        Member loginMember = (Member)session.getAttribute("loginMember");
//        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();
//
//        try {
//            if (!validateMemberRole(roleType)) {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        CodeQuestionResponseDTO responseDTO = codeQuestionService.findById(id);
//        codeQuestionService.updateViews(id); // views++
//
//        //댓글
//        Long totalComments = codeQuestionCommentRepository.countByCodeQuestionId(id);
//        Page<CodeQuestionCommentResponseDTO> codeQuestionCommentDTOPage = codeQuestionCommentService.findAllWithChild2(id, pageable);
//        responseDTO.setCodeQuestionComment(codeQuestionCommentDTOPage);
//        responseDTO.setTotalComments(totalComments);
//
//        model.addAttribute("codeQuestionCommentList", codeQuestionCommentDTOPage);
//        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//    }
//
//    /* 게시글 제목 검색 */
//    @GetMapping("/codequestion/search")
//    public ResponseEntity<Page<CodeQuestionListResponseDTO>> searchByTitle(String title, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
//        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.searchByTitle(title, pageable);
//        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
//    }
//
//    /* 게시글 작성자 검색 */
//    @GetMapping("/codequestion/searchByWriter")
//    public ResponseEntity<Page<CodeQuestionListResponseDTO>> searchByWriter(String writer, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
//        Page<CodeQuestionListResponseDTO> responseDTOS = codeQuestionService.searchByWriter(writer, pageable);
//        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
//    }
//
//    private boolean validateMemberRole(RoleType roleType) {
//        return (roleType == RoleType.USER) || (roleType == RoleType.ADMIN);
//    }
//}
