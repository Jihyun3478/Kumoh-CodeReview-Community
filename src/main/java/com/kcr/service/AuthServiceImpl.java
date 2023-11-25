package com.kcr.service;

import com.kcr.domain.dto.member.login.LoginRequestDTO;
import com.kcr.domain.dto.member.signup.SignupRequestDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.exception.CustomException;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.repository.QuestionRepository;
import com.kcr.service.util.JwtUtil;
import com.kcr.service.util.RedisUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.kcr.domain.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailServiceImpl emailServiceImpl;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
//    private final SaltUtil saltUtil;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

//    @Transactional
//    public void signup(Member member) {
//        String password = member.getLoginPw();
//        String salt = saltUtil.genSalt();
//        member.setSalt(new Salt(salt));
//        member.setLoginPw(saltUtil.encodePassword(salt,password));
//        memberRepository.save(member);
//    }
//
//    public Member login(String loginId, String loginPw) throws Exception{
//        Member member = memberRepository.findByLoginId(loginId);
//        if(member==null) throw new Exception ("멤버가 조회되지 않음");
//
//        String salt = member.getSalt().getSalt();
//        loginPw = saltUtil.encodePassword(salt,loginPw);
//        if(!member.getLoginPw().equals(loginPw))
//            throw new Exception ("비밀번호가 틀립니다.");
//        return member;
//    }

    /* 회원 가입 */
    @Transactional
    public void signup(SignupRequestDTO requestDTO) {
        String loginId = requestDTO.getLoginId() + "@kumoh.ac.kr";
        String loginPw = passwordEncoder.encode(requestDTO.getLoginPw());
        String nickname = requestDTO.getNickname();
        String stuNum = requestDTO.getStuNum();

        // 회원 중복 확인
        Member checkUsername = memberRepository.findByLoginId(loginId);
        if (!(checkUsername == null)) {
            throw new CustomException(DUPLICATED_USERNAME);
        }

        // 사용자 ROLE 확인 (admin = true 일 경우 아래 코드 수행)
        RoleType role = RoleType.NOT_PERMITTED;
        if (requestDTO.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDTO.getAdminToken())) {
                throw new CustomException(NOT_MATCH_ADMIN_TOKEN);
            }
            role = RoleType.ADMIN;
        }

        // 사용자 등록 (admin = false 일 경우 아래 코드 수행)
        Member member = new Member(loginId, loginPw, role, nickname, stuNum);
        memberRepository.save(member);
    }

    /* 로그인 */
    @Transactional
    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        String loginId = loginRequestDTO.getLoginId();
        String loginPw = loginRequestDTO.getLoginPw();

//        Member member = memberRepository.findByLoginId(loginId).orElseThrow(
//                () -> new CustomException(NOT_MATCH_INFORMATION)
//        );
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) {
            throw new CustomException(NOT_MATCH_INFORMATION);
        }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(loginPw, member.getLoginPw())) {
            throw new CustomException(NOT_MATCH_INFORMATION);
        }

        // Header 에 key 값과 Token 담기
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, JwtUtil.createToken(member.getLoginId(), member.getRoleType()));
    }

    @Override
    @Transactional
    public Member findByLoginId(String loginId) throws NotFoundException {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
        return member;
    }

    /* 이메일 확인 시 RoleType.User로 변경 */
    @Override
    @Transactional
    public void verifyEmail(String key) throws NotFoundException {
        String loginId = redisUtil.getData(key);
        Member member = memberRepository.findByLoginId(loginId);

        if(member == null) throw new NotFoundException("멤버가 조회되지않음");
        modifyUserRole(member, RoleType.USER);

        redisUtil.deleteData(key);
    }

    /* 인증 메일 전송 */
    @Override
    @Transactional
    public void sendVerificationMail(Member member) throws NotFoundException {
        String VERIFICATION_LINK = "http://localhost:8080/api/signup/verify/";
        if(member == null) throw new NotFoundException("멤버가 조회되지 않음");
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
