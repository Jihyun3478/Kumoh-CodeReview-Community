package com.kcr.service;

import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import javassist.NotFoundException;

import java.util.Optional;

public interface AuthService {
//    void signup(Member member);
//
//    Member login(String loginId, String loginPw) throws Exception;
    final String REDIS_CHANGE_PASSWORD_PREFIX="CPW";

    Member findByLoginId(String LoginId) throws NotFoundException;

    void verifyEmail(String key) throws NotFoundException;

    void sendVerificationMail(Member member) throws NotFoundException;

    void modifyUserRole(Member member, RoleType roleType);

//    boolean isPasswordUuidValidate(String key);

//    void changePassword(Member member, String password) throws NotFoundException;

//    void requestChangePassword(Member member) throws NotFoundException;
}
