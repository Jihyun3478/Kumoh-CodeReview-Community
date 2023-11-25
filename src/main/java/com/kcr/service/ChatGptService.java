package com.kcr.service;

import com.kcr.config.ChatGptConfig;
import com.kcr.domain.dto.chatGPT.ChatGptMessage;
import com.kcr.domain.dto.chatGPT.ChatGptRequest;
import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.entity.ChatGPT;
import com.kcr.domain.entity.Question;
import com.kcr.repository.ChatGPTRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatGptService {
    private final RestTemplate restTemplate;
    @Autowired
    private ChatGPTRepository chatGPTRepository;
    @Autowired
    private QuestionRepository questionRepository;
    //yml.에 있는 api key파일
    @Value("${openai.api.token}")
    private String apiKey;

    //askquestion으로 매핑된 객체에 헤더정보를 포함하는 함수
    @Transactional
    public HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest chatGptRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        httpHeaders.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);

        System.out.println("chatRequest " + chatGptRequest);
        return new HttpEntity<>(chatGptRequest, httpHeaders);
    }

    //질문보내고 답을 받음
    @Transactional
    public ChatGptResponse getResponse(Question question,HttpEntity<ChatGptRequest> chatGptRequestHttpEntity) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        //답변이 길어질 경우 TimeOut Error가 발생하니 1분정도 설정
        requestFactory.setReadTimeout(60 * 1000);   //  1min = 60 sec * 1,000ms
        restTemplate.setRequestFactory(requestFactory);
        System.out.println("확인 "+chatGptRequestHttpEntity.getBody().getMessages().get(0).getContent());
        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.CHAT_URL,
                chatGptRequestHttpEntity,
                ChatGptResponse.class);
        responseEntity.getBody().setQuestion(question);
        return responseEntity.getBody();
    }
    @Transactional
    public ChatGptResponse askQuestion2(Question question,ChatGptResponse chatGptResponse) {
        List<ChatGptMessage> messages = new ArrayList<>();
       /* Question question = questionRepository.findById(questionID).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + questionID));*/
        //  chatGPT.setQuestion(question);
      //  Question question = resquestDTO.toSaveEntity();
        // System.out.println("question id : "+question.getId());
        messages.add(ChatGptMessage.builder()
                .role(ChatGptConfig.ROLE)
                .content(question.getContent())
                .build());
        // 이 구조는 cascade 메소드다
        System.out.println("askquestion message: " + messages.get(0).getContent());

        return this.getResponse( //세번째 실행,
                question,
                this.buildHttpEntity( //두번째 실행
                        new ChatGptRequest( // 첫번째 실행
                                ChatGptConfig.CHAT_MODEL,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.STREAM,
                                messages
                                //ChatGptConfig.TOP_P
                        )
                )
        );
    }
    //챗지피티 조회
    @Transactional
    public ChatGptResponse findById(Long id) {
        Optional<ChatGPT> chatGPT = chatGPTRepository.findById(id);
        return ChatGptResponse.builder()
                .id(chatGPT.get().getId())
                .content(chatGPT.get().getGptContent())
                .build();
    }
}