package com.kcr.domain.dto.chatGPT;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatGptMessage {
    private String role;
    private String content;
    @Builder
    public ChatGptMessage(String role, String content){
        this.role = role;
        this.content=content;
    }
}
