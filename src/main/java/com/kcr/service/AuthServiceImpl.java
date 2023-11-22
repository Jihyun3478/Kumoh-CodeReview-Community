package com.kcr.service;

import com.kcr.domain.dto.member.login.LoginRequestDTO;
import com.kcr.domain.dto.member.signup.SignupRequestDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Salt;
import com.kcr.domain.exception.CustomException;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.service.util.JwtUtil;
import com.kcr.service.util.RedisUtil;
import com.kcr.service.util.SaltUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

import static com.kcr.domain.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailServiceImpl emailServiceImpl;
    private final MemberRepository memberRepository;
    private final SaltUtil saltUtil;
    private final RedisUtil redisUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원 가입
//    public void signup(SignupRequestDTO requestDTO) {
//        String loginId = requestDTO.getLoginId();
//        String loginPw = passwordEncoder.encode(requestDTO.getLoginPw());
//
//        // 회원 중복 확인
//        Optional<Member> checkUsername = memberRepository.findByLoginId(loginId);
//        if (checkUsername.isPresent()) {
//            throw new CustomException(DUPLICATED_USERNAME);
//        }
//
//        // 사용자 ROLE 확인 (admin = true 일 경우 아래 코드 수행)
//        RoleType role = RoleType.USER;
//        if (requestDTO.isAdmin()) {
//            if (!ADMIN_TOKEN.equals(requestDTO.getAdminToken())) {
//                throw new CustomException(NOT_MATCH_ADMIN_TOKEN);
//            }
//
//            role = RoleType.ADMIN;
//        }
//
//        // 사용자 등록 (admin = false 일 경우 아래 코드 수행)
//        Member member = new Member(loginId, loginPw, role);
//        memberRepository.save(member);
//    }

    // 로그인
//    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
//        String loginId = loginRequestDTO.getLoginId();
//        String loginPw = loginRequestDTO.getLoginPw();
//
//        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
//                () -> new CustomException(NOT_MATCH_INFORMATION)
//        );
//
//        // 비밀번호 일치 여부 확인
//        if (!passwordEncoder.matches(loginPw, member.getLoginPw())) {
//            throw new CustomException(NOT_MATCH_INFORMATION);
//        }
//
//        // Header 에 key 값과 Token 담기
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.createToken(member.getLoginId(), member.getRoleType()));
//    }

    @Override
    @Transactional
    public void signUp(Member member) {
        String loginId = member.getLoginId();
        String loginPw = member.getLoginPw();
        String salt = saltUtil.genSalt();

        member.setLoginId(loginId + "@kumoh.ac.kr");
        member.setSalt(new Salt(salt));
        member.setLoginPw(saltUtil.encodePassword(salt, loginPw));
        memberRepository.save(member);
    }

    @Override
    public Member signIn(String loginId, String loginPw) throws IllegalArgumentException {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) {
            throw new IllegalArgumentException("멤버가 조회되지 않음");
        }

        String salt = member.getSalt().getSalt();
        loginPw = saltUtil.encodePassword(salt, loginPw);
        if(!member.getLoginPw().equals(loginPw)) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        return member;
    }

    @Override
    public Member findByLoginId(String loginId) throws NotFoundException {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        return member;
    }

    @Override
    public void verifyEmail(String key) throws NotFoundException {
        String loginId = redisUtil.getData(key);
        Member member = memberRepository.findByLoginId(loginId);

        if(member==null) throw new NotFoundException("멤버가 조회되지않음");
        modifyUserRole(member, RoleType.USER);

        redisUtil.deleteData(key);
    }

    @Override
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/signup/verify/";
        if(member==null) throw new NotFoundException("멤버가 조회되지 않음");
        UUID uuid = UUID.randomUUID();

        redisUtil.setDataExpire(uuid.toString(), member.getLoginId(), 60 * 30L);
        emailServiceImpl.sendMail(member.getLoginId(),"[KCR] 회원가입 인증 메일입니다.",VERIFICATION_LINK+uuid.toString());
    }

    @Override
    public void modifyUserRole(Member member, RoleType roleType){
        member.setRoleType(roleType);
        memberRepository.save(member);
    }
}
