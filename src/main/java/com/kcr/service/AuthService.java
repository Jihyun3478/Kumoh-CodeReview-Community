package com.kcr.service;

import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import javassist.NotFoundException;

public interface AuthService {
    void signUp(Member member);

    Member signIn(String loginId, String loginPw);
    final String REDIS_CHANGE_PASSWORD_PREFIX="CPW";

    Member findByLoginId(String LoginId) throws NotFoundException;

    void verifyEmail(String key) throws NotFoundException;

    void sendVerificationMail(Member member) throws NotFoundException;

    void modifyUserRole(Member member, RoleType roleType);
//
//    boolean isPasswordUuidValidate(String key);
//
//    void changePassword(Member member, String password) throws NotFoundException;
//
//    void requestChangePassword(Member member) throws NotFoundException;
}
