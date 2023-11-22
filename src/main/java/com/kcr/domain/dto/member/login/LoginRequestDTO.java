package com.kcr.domain.dto.member.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
public class LoginRequestDTO {
    private final String loginId;
    private final String loginPw;

    @JsonCreator
    public LoginRequestDTO(@JsonProperty("loginId")String loginId, @JsonProperty("loginPw")String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }
}