package com.kcr.domain.entity;

import com.kcr.domain.type.RoleType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@Table(name = "members")
@NoArgsConstructor // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
@AllArgsConstructor
//@Builder
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String stuNum;

    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.NOT_PERMITTED;

    private Long activityScore;

    public Member(String loginId, String loginPw, RoleType roleType, String nickname, String stuNum) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.roleType = roleType;
        this.nickname = nickname;
        this.stuNum = stuNum;
    }
}
