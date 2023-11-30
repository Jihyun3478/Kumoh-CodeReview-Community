package com.kcr.domain.dto.member;

import com.kcr.domain.entity.Member;
import com.kcr.domain.type.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter

public class MemberListResponseDTO {
    private final Long id;
    private final String loginId;
    private final String loginPw;
    private final String nickname;
    private final String stuNum;
    private final RoleType roleType;

    /* Entity -> DTO */
    @Builder
    public MemberListResponseDTO(Long id, String loginId, String loginPw, String nickname, String stuNum, RoleType roleType) {
        this.id = id;
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.nickname = nickname;
        this.stuNum = stuNum;
        this.roleType = roleType;
    }

    public MemberListResponseDTO(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.loginPw = member.getLoginPw();
        this.nickname = member.getNickname();
        this.stuNum = member.getStuNum();
        this.roleType = member.getRoleType();
    }
}