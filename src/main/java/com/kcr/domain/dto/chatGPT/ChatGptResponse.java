package com.kcr.domain.dto.chatGPT;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kcr.domain.entity.ChatGPT;
import com.kcr.domain.entity.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
//ChatGPT 답변을 담을 DTO
public class ChatGptResponse {
    private  Long gptId;
    private String id;
    private String object;
    private long created;
    private String model;
    private Usage usage;
    private List<Choice> choices;
    //private Long questionID;
    private Question question;
    private String content;

    /* DTO -> Entity */

    public ChatGPT toSaveEntity() {
        return ChatGPT.builder()
                .id(gptId)
                .gptContent(choices.get(0).getMessage().getContent())
                .question(question)
                .build();
    }

    @Builder
    public ChatGptResponse(Long id, String content) {
        this.gptId = id;
        this.content=content;
    }
    @Getter
    @Setter
    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
    }
    @Getter
    @Setter
    public static class Choice {
        private ChatGptMessage message;
        @JsonProperty("finish_reason")
        private String finishReason;
        private int index;
    }
}

