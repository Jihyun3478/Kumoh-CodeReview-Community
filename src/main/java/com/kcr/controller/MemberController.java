package com.kcr.controller;

import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.member.login.LoginRequestDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.service.AuthServiceImpl;
import com.kcr.service.util.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final AuthServiceImpl authServiceImpl;
    private final MemberRepository memberRepository;

    /* ================ API ================ */
    /* 회원가입 */
    @PostMapping("/api/signup")
    public ResponseEntity<MsgResponseDTO> signup(@RequestBody @Valid Member loginMember) {
        if(loginMember == null) {
            return ResponseEntity.ok(new MsgResponseDTO("회원가입 완료", HttpStatus.OK.value()));
        }
        authServiceImpl.signup(loginMember);

        try {
            Member member = authServiceImpl.findByLoginId(loginMember.getLoginId() + "@kumoh.ac.kr");
            authServiceImpl.sendVerificationMail(member);
            return ResponseEntity.ok(new MsgResponseDTO("인증 메일 전송 완료", HttpStatus.OK.value()));
        } catch (Exception exception) {
            return ResponseEntity.ok(new MsgResponseDTO("인증 메일 전송 실패", HttpStatus.BAD_REQUEST.value()));
        }
    }

    /* 로그인 */
    @PostMapping("/api/signin")
    public ResponseEntity<MsgResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto, BindingResult bindingResult, HttpServletRequest request) {
        String loginId = loginRequestDto.getLoginId();
        String loginPw = loginRequestDto.getLoginPw();

        Member member = memberRepository.findByLoginId(loginRequestDto.getLoginId());
        if (member == null || member.getLoginPw() == null) {
            try {
                throw new IllegalArgumentException("존재하지 않는 회원입니다.");
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(new MsgResponseDTO(e.getMessage(), HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
            }
        }

        validatePassword(loginPw, member);
//        Member loginMember = authServiceImpl.login(loginId, loginPw);
        authServiceImpl.login(loginId, loginPw);

        // 로그인 성공 처리
        HttpSession session = request.getSession();
        System.out.println(session.getId());

        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return ResponseEntity.ok(new MsgResponseDTO("로그인 완료", HttpStatus.OK.value()));
    }

//    @PostMapping("/api/signin")
//    public ResponseEntity<MsgResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto, BindingResult bindingResult, HttpServletRequest request) {
//        String loginId = loginRequestDto.getLoginId();
//        String loginPw = loginRequestDto.getLoginPw();
//
//        Member member = memberRepository.findByLoginId(loginRequestDto.getLoginId());
//        if(member == null) {
//            try {
//                throw new IllegalArgumentException();
//            } catch (IllegalArgumentException e) {
//                new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.FORBIDDEN);
//            }
//        }
//
//        if(member.getLoginPw() != null) {
//            validatePassword(loginPw, member);
//        }
//
//        Member loginMember = authServiceImpl.login(loginId, loginPw);
//
//        //로그인 성공 처리
//        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
//        HttpSession session = request.getSession();
//        System.out.println(session.getId());
//
//        //세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//        return ResponseEntity.ok(new MsgResponseDTO("로그인 완료", HttpStatus.OK.value()));
//    }

    private static void validatePassword(String loginPw, Member member) {
        try {
            if (!(Objects.equals(loginPw, member.getLoginPw()))) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/api/signout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    /* ================ UI ================ */
    /* 인증 메일 확인 */
    @GetMapping("/signup/verify/{key}")
    public ResponseEntity<MsgResponseDTO> getVerify(@PathVariable String key) {
        try {
            authServiceImpl.verifyEmail(key);
            return ResponseEntity.ok(new MsgResponseDTO("성공적으로 인증메일을 확인했습니다.", HttpStatus.OK.value()));

        } catch (Exception e) {
            return ResponseEntity.ok(new MsgResponseDTO("인증메일을 확인하는데 실패했습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }
}
