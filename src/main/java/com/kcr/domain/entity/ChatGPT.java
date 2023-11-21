package com.kcr.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "chatgpt")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatGPT {
    @Id @GeneratedValue
    @Column(name = "CHATGPT_ID")
    private Long id;

    @Column(name= "CONTENT")
    private String gptContent;

    @Column(name = "CREATEDATE")
    @CreatedDate
    private String createDate;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "QUESTION_ID")
    private Question question;

    public ChatGPT( Long id,String gptContent, Question question) {
        this.id=id;
        this.gptContent = gptContent;
        this.question = question;
    }
}

