package com.kcr.domain.dto.member;

import com.kcr.domain.entity.Member;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class SecurityMemberDTO extends User {
    private static final long serialVersionUiD = 1L;

    public SecurityMemberDTO(Member member){
        super(member.getLoginId(),"{noop}"+ member.getLoginPw(), AuthorityUtils.createAuthorityList(member.getRoleType().toString()));
    }

}