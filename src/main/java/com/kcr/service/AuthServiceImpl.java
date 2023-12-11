package com.kcr.service;

import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Salt;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.service.util.RedisUtil;
import com.kcr.service.util.SaltUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailServiceImpl emailServiceImpl;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final SaltUtil saltUtil;
    private static final String emailAddress = "@kumoh.ac.kr";

    @Transactional
    public void signup(Member member) {
        String loginId = member.getLoginId() + emailAddress;

        // 비밀번호 해쉬 암호화
        String loginPw = member.getLoginPw();
        String salt = saltUtil.genSalt();
        member.setLoginPw(saltUtil.encodePassword(salt,loginPw));

        String saltPw = member.getLoginPw();
        String nickname = member.getNickname();
        String stuNum = member.getStuNum();

        // 아이디 중복 확인
        Member checkLoginId = memberRepository.findByLoginId(loginId);
        if (!(checkLoginId == null)) {
            ResponseEntity.ok(new MsgResponseDTO("중복된 아이디입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        // 닉네임 중복 확인
        Member checkNickname = memberRepository.findByNickname(nickname);
        if (!(checkNickname == null)) {
            ResponseEntity.ok(new MsgResponseDTO("중복된 닉네임입니다.", HttpStatus.BAD_REQUEST.value()));
        }

        RoleType role = RoleType.USER;

        Member savedMember = new Member(loginId, saltPw, role, nickname, stuNum, new Salt(salt));
        memberRepository.save(savedMember);
    }

    @Transactional
    public Member login(String loginId, String loginPw) {
        Member member = memberRepository.findByLoginId(loginId);

        // 비밀번호 일치 여부 확인
        try {
            if (!(Objects.equals(loginPw, member.getLoginPw()))) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.FORBIDDEN);
        }
        String memberSalt = member.getSalt().getSalt();
        loginPw = saltUtil.encodePassword(memberSalt, loginPw);
        return new Member(loginId, loginPw);
    }

    @Override
    @Transactional
    public Member findByLoginId(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) {
            ResponseEntity.ok(new MsgResponseDTO("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST.value()));
        }
        return member;
    }

    /* 이메일 확인 시 RoleType.User로 변경 */
    @Override
    @Transactional
    public void verifyEmail(String key) {
        String loginId = redisUtil.getData(key);
        Member member = memberRepository.findByLoginId(loginId);

        if(member == null) {
            ResponseEntity.ok(new MsgResponseDTO("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST.value()));
        }
        modifyUserRole(member, RoleType.USER);

        redisUtil.deleteData(key);
    }

    /* 인증 메일 전송 */
    @Override
    @Transactional
    public void sendVerificationMail(Member member) {
        String VERIFICATION_LINK = "http://localhost:8080/signup/verify/";
        if(member == null) {
            ResponseEntity.ok(new MsgResponseDTO("존재하지 않는 회원입니다.", HttpStatus.BAD_REQUEST.value()));
        }
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
