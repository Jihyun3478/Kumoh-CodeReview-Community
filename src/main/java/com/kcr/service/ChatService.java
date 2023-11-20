package com.kcr.service;

import com.kcr.domain.dto.chatGPT.ChatGptConfig;
import com.kcr.domain.dto.chatGPT.ChatGptRequest;
import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.entity.Question;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatService {
    private static RestTemplate restTemplate = new RestTemplate();

    public HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponse.class);

        String gptAnswer = "";
        gptAnswer= responseEntity.getBody().getPrompt();
        System.out.println(gptAnswer);
        return responseEntity.getBody();
    }

    public ChatGptResponse askQuestion(Question requestQuestion) {
        System.out.println("질문은 : = "+requestQuestion);
       return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.MODEL,
                                requestQuestion.getContent(), //게시글 내용
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
    }
}