package com.kcr.domain.dto.member.login;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class LoginRequestDTO {
    @NotEmpty
    private final String loginId;
    @NotEmpty
    private final String loginPw;

    @JsonCreator
    public LoginRequestDTO(@JsonProperty("loginId")String loginId, @JsonProperty("loginPw")String loginPw) {
        this.loginId = loginId;
        this.loginPw = loginPw;
    }
}