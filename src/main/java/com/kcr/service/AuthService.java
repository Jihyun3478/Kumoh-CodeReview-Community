package com.kcr.service;

import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import javassist.NotFoundException;

import java.util.Optional;

public interface AuthService {

    Member findByLoginId(String LoginId) throws NotFoundException;

    void verifyEmail(String key) throws NotFoundException;

    void sendVerificationMail(Member member) throws NotFoundException;

    void modifyUserRole(Member member, RoleType roleType);
}
