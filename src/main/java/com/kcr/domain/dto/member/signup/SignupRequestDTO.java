package com.kcr.domain.dto.member.signup;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class SignupRequestDTO {
    @NotBlank
    @Size(min = 4,max = 10, message ="작성자명은 4자 이상 10자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "작성자명은 알파벳 소문자, 숫자만 사용 가능합니다.")
    private String loginId;

    @NotBlank
    @Size(min = 8,max = 15, message ="비밀번호는 8자 이상 15자 이하만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z_0-9]*$", message = "비밀번호는 알파벳 대소문자, 숫자만 사용 가능합니다.")
    private String loginPw;

    @NotBlank
    private String nickname;

    @NotBlank
    private String stuNum;

    private boolean admin = false;      // 디폴트 값은 false. 관리자 권한일 경우 true 로 변한다

    private String adminToken = "";
}
