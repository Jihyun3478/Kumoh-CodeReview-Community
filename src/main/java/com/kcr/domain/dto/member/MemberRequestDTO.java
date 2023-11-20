package com.kcr.domain.dto.member;

import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Salt;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

/* 회원가입과 회원 정보 수정을 처리할 요청 클래스 */
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDTO {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Column(nullable = false, unique = true)
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String loginPw;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "학번은 필수 입력 값입니다.")
    private String stuNum;

    private Salt salt;

    @Builder
    public MemberRequestDTO(String loginId, String loginPw, String nickname, String stuNum) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.nickname = nickname;
        this.stuNum = stuNum;
    }

    /* DTO -> Entity */
    public Member toSaveEntity() {

        return Member.builder()
                .loginId(loginId)
                .loginPw(loginPw)
                .nickname(nickname)
                .stuNum(stuNum)
                .build();
    }
}
