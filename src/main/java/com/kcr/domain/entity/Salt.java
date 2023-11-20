package com.kcr.domain.entity;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Salt {

    @Id @GeneratedValue
    @Column(name = "SALT_ID")
    private Long id;

    @NotEmpty
    private String salt;

    public Salt() {}

    public Salt(String salt) {
        this.salt = salt;
    }

    /* 연관관계 */
    @OneToOne(fetch = LAZY, mappedBy = "salt")
    private Member member;
}
