package com.kcr.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag {

    @Id
    @Column(name = "TAG_NAME")
    private String tagName;

    /* 연관관계 */
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CODE_QUESTION_ID")
    private CodeQuestion codeQuestion;
}
