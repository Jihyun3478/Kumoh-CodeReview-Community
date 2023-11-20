package com.kcr.domain.entity;

import com.kcr.domain.type.RoleType;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 제어를 Protected로 설정함으로써 무분별한 객체 생성을 예방함
@AllArgsConstructor
@Builder
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

    @Column(nullable = false, unique = true)
    private String stuNum;

    @Enumerated(EnumType.STRING)
    @Column
    private final RoleType roleType = RoleType.ROLE_NOT_PERMITTED;

    private Long activityScore;

    /* 연관관계 */
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "SALT_ID")
    private Salt salt;

    @OneToOne // (fetch = LAZY)
    @JoinColumn(name = "IMAGE_ID")
    private Image image;
}
