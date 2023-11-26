package com.kcr.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kcr.domain.type.RoleType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

    /* 연관관계 */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SALT_ID")
    private Salt salt;

    @OneToOne
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @OneToOne
    @JoinColumn(name = "CODE_QUESTION_ID")
    private CodeQuestion codeQuestion;

    @OneToOne
    @JoinColumn(name = "QUESTION_COMMENT_ID")
    private QuestionComment questionComment;

    @OneToOne
    @JoinColumn(name = "CODE_QUESTION_COMMENT_ID")
    private CodeQuestionComment codeQuestionComment;

    public Member(String loginId, String loginPw, RoleType roleType, String nickname, String stuNum) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.roleType = roleType;
        this.nickname = nickname;
        this.stuNum = stuNum;
    }
}
