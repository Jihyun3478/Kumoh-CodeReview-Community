package com.kcr.controller;

import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.member.login.LoginRequestDTO;
import com.kcr.domain.dto.member.mail.MailRequestDTO;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final AuthServiceImpl authServiceImpl;
    private final MemberRepository memberRepository;

    /* ================ API ================ */
    /* 회원가입 */
    @PostMapping("/api/signup")
    public ResponseEntity<MsgResponseDTO> signup(@RequestBody @Valid Member loginMember, BindingResult bindingResult) {
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
        Member loginMember = authServiceImpl.login(loginRequestDto.getLoginId(), loginRequestDto.getLoginPw());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        System.out.println(session.getId());

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return ResponseEntity.ok(new MsgResponseDTO("로그인 완료", HttpStatus.OK.value()));
    }

    @PostMapping("/api/signout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("a1234@kumoh.ac.kr", "a1234", RoleType.USER, "사용자1", "20200930"));
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
