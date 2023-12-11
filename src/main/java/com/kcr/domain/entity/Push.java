package com.kcr.domain.entity;

import com.kcr.domain.type.PushType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "push")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Push {

    @Id @GeneratedValue
    @Column(name = "PUSH_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private PushType pushType;

    private LocalDateTime createDate;
    private boolean status;
}
