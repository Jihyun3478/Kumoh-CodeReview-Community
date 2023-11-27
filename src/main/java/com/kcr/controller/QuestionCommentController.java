package com.kcr.controller;

import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.QuestionComment;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.service.QuestionCommentService;
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
public class QuestionCommentController {

    private final QuestionCommentService questionCommentService;
    private final QuestionCommentRepository questionCommentRepository;
    private final MemberRepository memberRepository;

    //댓글등록
    @PostMapping("/question/{id}/comment")
    public ResponseEntity<QuestionCommentResponseDTO> commentSave(@PathVariable Long id, @RequestBody QuestionCommentRequestDTO questionCommentRequestDTO, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        RoleType roleType = memberRepository.findByLoginId(loginMember.getLoginId()).getRoleType();
        try {
            if(!validateMemberRole(roleType)) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        questionCommentRequestDTO.setWriter(loginMember.getNickname());
        return new ResponseEntity<>(questionCommentService.commentSave(id, questionCommentRequestDTO), HttpStatus.OK);
    }

    @PatchMapping("/question/{questionId}/comments/{id}")
    public ResponseEntity<Long> update(@PathVariable Long questionId,
                                                      @PathVariable Long id,
                                                      @RequestBody QuestionCommentRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        String writer = questionCommentRepository.findById(id).get().getWriter();

        System.out.println("loginNickname = " + loginNickname.getNickname());
        try {
            if (!Objects.equals(writer, loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        questionCommentService.updateComment(questionId, id, requestDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}/delete")
    public ResponseEntity<MsgResponseDTO> delete(@PathVariable("id") Long id, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");

        Member loginNickname = memberRepository.findByNickname(loginMember.getNickname());
        QuestionComment questionComment = questionCommentRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        });

        try {
            if (!Objects.equals(questionComment.getWriter(), loginNickname.getNickname())) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(questionCommentService.delete(id), HttpStatus.OK);
    }

    //대댓글 등록
    @PostMapping("/question/{id}/comment/{parentId}/child")
    public ResponseEntity<QuestionCommentResponseDTO> createChildComment(@PathVariable Long parentId,@PathVariable Long id,
                                                                         @RequestBody QuestionCommentRequestDTO requestDTO, HttpSession session) {
        Member loginMember = (Member)session.getAttribute("loginMember");
        requestDTO.setWriter(loginMember.getNickname());

        return new ResponseEntity<>(questionCommentService.saveChildComment(parentId, id, requestDTO), HttpStatus.OK);
    }

    // 대댓글만 조회(테스트용)
    @GetMapping("/question/{id}/comment/{commentId}/child")
    public ResponseEntity<List<QuestionCommentResponseDTO>> getChildComments(@PathVariable Long commentId) {
        List<QuestionCommentResponseDTO> childComments = questionCommentService.findAllChildComments(commentId);
        return new ResponseEntity<>(childComments, HttpStatus.OK);
    }

    private boolean validateMemberRole(RoleType roleType) {
        return (roleType == RoleType.USER) || (roleType == RoleType.ADMIN);
    }
}