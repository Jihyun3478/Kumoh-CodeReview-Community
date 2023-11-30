package com.kcr.service;

import com.kcr.domain.dto.member.login.LoginRequestDTO;
import com.kcr.domain.dto.member.signup.SignupRequestDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@SpringBootTest
class AuthServiceImplTest {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Test
    void signUpAndSignIn() {
        // 회원가입
        SignupRequestDTO signupRequestDTO = new SignupRequestDTO();
        signupRequestDTO.setLoginId("hun3478");
        signupRequestDTO.setLoginPw("hoon0815");
        signupRequestDTO.setNickname("jihyun");
        signupRequestDTO.setStuNum("20200930");

        String id = signupRequestDTO.getLoginId() + "@kumoh.ac.kr";
        String pw = passwordEncoder.encode(signupRequestDTO.getLoginPw());
        String nickname = signupRequestDTO.getNickname();
        String stuNum = signupRequestDTO.getStuNum();

        RoleType role = RoleType.NOT_PERMITTED;

        Member member = new Member(id, pw, role, nickname, stuNum);
        memberRepository.save(member);

        // 로그인
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(signupRequestDTO.getLoginId(), signupRequestDTO.getLoginPw());
        String loginId = loginRequestDTO.getLoginId();
        Member member2 = memberRepository.findByLoginId(loginId);

        // Header 에 key 값과 Token 담기
        JwtUtil.createToken(member2.getLoginId(), member2.getRoleType());
    }
}