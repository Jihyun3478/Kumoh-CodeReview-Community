package com.kcr.service;

import com.kcr.domain.dto.member.MemberRequestDTO;
import com.kcr.domain.entity.Salt;
import com.kcr.repository.MemberRepository;
import com.kcr.service.util.SaltUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final SaltUtil saltUtil;

    @Override
    @Transactional
    public void signUp(MemberRequestDTO requestDTO) {
        String loginPw = requestDTO.getLoginPw();
        String salt = saltUtil.genSalt();

        requestDTO.setSalt(new Salt(salt));
        requestDTO.setLoginPw(saltUtil.encodePassword(salt, loginPw));
        memberRepository.save(requestDTO.toSaveEntity());
    }

    @Override
    public MemberRequestDTO signIn(String loginId, String loginPw) throws IllegalArgumentException {
        MemberRequestDTO requestDTO = memberRepository.findByLoginId(loginId);
        if(requestDTO == null) {
            throw new IllegalArgumentException("멤버가 조회되지 않음");
        }

        String salt = requestDTO.getSalt().getSalt();
        loginPw = saltUtil.encodePassword(salt, loginPw);
        if(!requestDTO.getLoginPw().equals(loginPw)) {
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        return requestDTO;
    }
}
