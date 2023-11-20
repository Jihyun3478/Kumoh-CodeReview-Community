package com.kcr.controller.api;

import com.kcr.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat-gpt")
@Slf4j
@RequiredArgsConstructor
public class ChatGPTApiController {
    private final ChatService chatgptService;
    /*@PostMapping("")
    public ChatGptResponse askGPT2(@RequestBody Question question){
        System.out.println(question);
        return chatgptService.askQuestion(question);
    }*/
    /* public QuestionCommentController(ChatService chatgptService) {
        this.chatgptService = chatgptService;
    }*/
    //게시글 올라오자마자 댓글 자동응답
    /*@PostMapping("")
    public String askGPT(Long id, @RequestBody String question){
        //question = "안녕 gpt! 방탄소년단의 멤버를 알려줘!";

        System.out.println(question);
        questionCommentService.putQuestion2(id, question);

        return "";
        //\n\nAs an AI language model, I don't have feelings, but I'm functioning well. Thank you for asking. How can I assist you today?
    }*/
}
