package com.kcr.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id @GeneratedValue
    @Column(name = "IMAGE_ID")
    private Long id;

    private String url;

    /* 연관관계 */
    @OneToOne(fetch = LAZY, mappedBy = "image")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CODE_QUESTION_ID")
    private CodeQuestion codeQuestion;
}
