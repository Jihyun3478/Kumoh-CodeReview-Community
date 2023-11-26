package com.kcr.service;

import com.kcr.domain.entity.Member;
import com.kcr.domain.exception.CustomException;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.service.util.RedisUtil;
import com.kcr.service.util.SaltUtil;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.kcr.domain.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MailServiceImpl emailServiceImpl;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;
    private final SaltUtil saltUtil;
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private static final String emailAddress = "@kumoh.ac.kr";

    @Transactional
    public void signup(Member member) {
        String loginId = member.getLoginId() + emailAddress;

        // 비밀번호 해쉬 암호화
//        String loginPw = member.getLoginPw();
//        String salt = saltUtil.genSalt();
//        member.setSalt(new Salt(salt));
//        member.setLoginPw(saltUtil.encodePassword(salt,loginPw));

        String saltPw = member.getLoginPw();
        String nickname = member.getNickname();
        String stuNum = member.getStuNum();

        // 아이디 중복 확인
        Member checkLoginId = memberRepository.findByLoginId(loginId);
        if (!(checkLoginId == null)) {
            throw new CustomException(DUPLICATED_ID);
        }

        // 닉네임 중복 확인
        Member checkNickname = memberRepository.findByNickname(nickname);
        if (!(checkNickname == null)) {
            throw new CustomException(DUPLICATED_NICKNAME);
        }

        RoleType role = RoleType.NOT_PERMITTED;

        Member savedMember = new Member(loginId, saltPw, role, nickname, stuNum);
        memberRepository.save(savedMember);
    }

    @Transactional
    public Member login(String loginId, String loginPw) {
        Member member = memberRepository.findByLoginId(loginId);
        if(member == null) {
            throw new CustomException(NOT_MATCH_INFORMATION);
        }

//        String memberSalt = member.getSalt().getSalt();
//        loginPw = saltUtil.encodePassword(memberSalt, loginPw);

        // 비밀번호 일치 여부 확인
        if (!(loginPw.equals(member.getLoginPw()))) {
            throw new CustomException(NOT_MATCH_INFORMATION);
        }
        return member;
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
