package com.kcr.service;

import com.kcr.domain.dto.member.MemberRequestDTO;

public interface AuthService {
    void signUp(MemberRequestDTO requestDTO);

    MemberRequestDTO signIn(String loginId, String loginPw);
}
