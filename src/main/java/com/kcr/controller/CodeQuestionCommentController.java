package com.kcr.controller;

import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentRequestDTO;
import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.entity.CodeQuestionComment;
import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.CodeQuestionCommentRepository;
import com.kcr.repository.MemberRepository;
import com.kcr.service.CodeQuestionCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CodeQuestionCommentController {

    private final CodeQuestionCommentService codeQuestionCommentService;
    private final MemberRepository memberRepository;
    private final CodeQuestionCommentRepository codeQuestionCommentRepository;

    //댓글 수정 , 부분수정이여서 PatchMapping 으로 바꿈
//    @PatchMapping("/codequestion/{codequestionid}/codecomment/{id}")
//    public ResponseEntity<Long> update(@PathVariable Long codequestionid,
//                                       @PathVariable Long id,
//                                       @RequestBody CodeQuestionCommentRequestDTO requestDTO, HttpSession session) {
//        Member loginMember = (Member)session.getAttribute("loginMember");
//
//        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
//        if(!Objects.equals(requestDTO.getWriter(), loginNickname.getNickname())) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        codeQuestionCommentService.updateComment(codequestionid, id, requestDTO);
//        return new ResponseEntity<>(id, HttpStatus.OK);
//    }

    @PatchMapping("/codequestion/{codequestionid}/codecomment/{id}")
    public ResponseEntity<Long> update(@PathVariable Long questionId,
                                       @PathVariable Long id,
                                       @RequestBody CodeQuestionCommentRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        String writer = codeQuestionCommentRepository.findById(id).get().getWriter();

        System.out.println("loginNickname = " + loginNickname.getNickname());
        try {
            if (!Objects.equals(writer, loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        codeQuestionCommentService.updateComment(questionId, id, requestDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/codequestion/{codequestionid}/codecomment/{id}/delete")
    public ResponseEntity<MsgResponseDTO> delete(@PathVariable("id") Long id, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        CodeQuestionComment codeQuestionComment = codeQuestionCommentRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        });

        try {
            if (!Objects.equals(codeQuestionComment.getWriter(), loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(codeQuestionCommentService.delete(id), HttpStatus.OK);
    }


    //댓글등록
    @PostMapping("/codequestion/{id}/codecomment")
    public ResponseEntity<CodeQuestionCommentResponseDTO> commentSave(@PathVariable Long id, @RequestBody CodeQuestionCommentRequestDTO codeQuestionCommentRequestDTO, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();
        try {
            if(!validateMemberRole(roleType)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        codeQuestionCommentRequestDTO.setWriter(loginMember.getNickname());
        return new ResponseEntity<>(codeQuestionCommentService.commentSave(id, codeQuestionCommentRequestDTO), HttpStatus.OK);
    }

    //대댓글 등록
    @PostMapping("/codequestion/{id}/codecomment/{parentId}/child")
    public ResponseEntity<CodeQuestionCommentResponseDTO> createChildComment(@PathVariable Long parentId, @PathVariable Long id,
                                                                             @RequestBody CodeQuestionCommentRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");
        requestDTO.setWriter(loginMember.getNickname());

        return new ResponseEntity<>(codeQuestionCommentService.saveChildComment(parentId, id, requestDTO), HttpStatus.OK);
    }


    // 대댓글만 조회(테스트용)
    @GetMapping("/codequestion/{id}/codecomment/{commentId}/children")
    public ResponseEntity<List<CodeQuestionCommentResponseDTO>> getChildComments(@PathVariable Long commentId) {
        List<CodeQuestionCommentResponseDTO> childComments = codeQuestionCommentService.findAllChildComments(commentId);
        return new ResponseEntity<>(childComments, HttpStatus.OK);
    }

    private boolean validateMemberRole(RoleType roleType) {
        return (roleType == RoleType.USER) || (roleType == RoleType.ADMIN);
    }
}
//package com.kcr.controller;
//
//import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentRequestDTO;
//import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
//import com.kcr.domain.dto.member.MsgResponseDTO;
//import com.kcr.domain.entity.CodeQuestionComment;
//import com.kcr.domain.entity.Member;
//import com.kcr.domain.type.RoleType;
//import com.kcr.repository.CodeQuestionCommentRepository;
//import com.kcr.repository.MemberRepository;
//import com.kcr.service.CodeQuestionCommentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpSession;
//import java.util.List;
//import java.util.Objects;
//
//@RestController
//@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")
//public class CodeQuestionCommentController {
//
//    private final CodeQuestionCommentService codeQuestionCommentService;
//    private final MemberRepository memberRepository;
//    private final CodeQuestionCommentRepository codeQuestionCommentRepository;
//
//    @PatchMapping("/codequestion/{codequestionid}/codecomment/{id}")
//    public ResponseEntity<Long> update(@PathVariable Long questionId,
//                                       @PathVariable Long id,
//                                       @RequestBody CodeQuestionCommentRequestDTO requestDTO, HttpSession session) {
//        Member loginMember = (Member) session.getAttribute("loginMember");
//
//        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
//        String writer = codeQuestionCommentRepository.findById(id).get().getWriter();
//
//        System.out.println("loginNickname = " + loginNickname.getNickname());
//        try {
//            if (!Objects.equals(writer, loginNickname.getNickname())) {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        codeQuestionCommentService.updateComment(questionId, id, requestDTO);
//        return new ResponseEntity<>(id, HttpStatus.OK);
//    }
//
//    // 댓글 삭제
//    @DeleteMapping("/codequestion/{codequestionid}/codecomment/{id}/delete")
//    public ResponseEntity<MsgResponseDTO> delete(@PathVariable("id") Long id, HttpSession session) {
//        Member loginMember = (Member)session.getAttribute("loginMember");
//
//        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
//        CodeQuestionComment codeQuestionComment = codeQuestionCommentRepository.findById(id).orElseThrow(() -> {
//            return new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
//        });
//
//        try {
//            if (!Objects.equals(codeQuestionComment.getWriter(), loginNickname.getNickname())) {
//                throw new IllegalArgumentException();
//            }
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//        return new ResponseEntity<>(codeQuestionCommentService.delete(id), HttpStatus.OK);
//    }
//
//
//    //댓글등록
//    @PostMapping("/codequestion/{id}/codecomment")
//    public ResponseEntity<CodeQuestionCommentResponseDTO> commentSave(@PathVariable Long id, @RequestBody CodeQuestionCommentRequestDTO codeQuestionCommentRequestDTO, HttpSession session) {
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
//        codeQuestionCommentRequestDTO.setWriter(loginMember.getNickname());
//        return new ResponseEntity<>(codeQuestionCommentService.commentSave(id, codeQuestionCommentRequestDTO), HttpStatus.OK);
//    }
//
//    //대댓글 등록
//    @PostMapping("/codequestion/{id}/codecomment/{parentId}/child")
//    public ResponseEntity<CodeQuestionCommentResponseDTO> createChildComment(@PathVariable Long parentId, @PathVariable Long id,
//                                                                         @RequestBody CodeQuestionCommentRequestDTO requestDTO, HttpSession session) {
//        Member loginMember = (Member)session.getAttribute("loginMember");
//        requestDTO.setWriter(loginMember.getNickname());
//
//        return new ResponseEntity<>(codeQuestionCommentService.saveChildComment(parentId, id, requestDTO), HttpStatus.OK);
//    }
//
//
//    // 대댓글만 조회(테스트용)
//    @GetMapping("/codequestion/{id}/codecomment/{commentId}/children")
//    public ResponseEntity<List<CodeQuestionCommentResponseDTO>> getChildComments(@PathVariable Long commentId) {
//        List<CodeQuestionCommentResponseDTO> childComments = codeQuestionCommentService.findAllChildComments(commentId);
//        return new ResponseEntity<>(childComments, HttpStatus.OK);
//    }
//
//    private boolean validateMemberRole(RoleType roleType) {
//        return (roleType == RoleType.USER) || (roleType == RoleType.ADMIN);
//    }
//}