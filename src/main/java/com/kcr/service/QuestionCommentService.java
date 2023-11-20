package com.kcr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.entity.QuestionComment;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class QuestionCommentService implements CommentService{

    private final QuestionCommentRepository questionCommentRepository;
    private final QuestionRepository questionRepository;
    private ChatService chatgptService;

    private static RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();


    //댓글등록
    public Long save(QuestionCommentRequestDTO questionCommentRequestDTO) {
        return questionCommentRepository.save(questionCommentRequestDTO.toSaveEntity()).getId();
    }

    @Override
    public void delete(Long id) {
        questionCommentRepository.deleteById(id);
    }

    public QuestionComment view(Long id) {

        return questionCommentRepository.findById(id).get();
    }

    /*public void update(Long id, QuestionComment questionComment) {
        QuestionComment updateComment = view(questionComment.getQuestion_comment_id());
        System.out.println(questionComment.getQuestion_comment_id()+" "+questionComment.getContent());
        updateComment.setContent(questionComment.getContent());
        System.out.println(questionComment.getContent()+" ");
        questionCommentRepository.save(updateComment);

    }*/
    //댓글수정
    public void update(Long id, QuestionCommentRequestDTO requestQuestionCommentDTO) {
       QuestionComment questionComment = questionCommentRepository.findById(id).orElseThrow(()->{
            return new IllegalArgumentException("수정에 실패했습니다.");
        });
        questionComment.updateQuestionComment(requestQuestionCommentDTO.getContent());
        System.out.println(questionComment.getContent()+" 수정된 content Service");
        //questionCommentRepository.save(requestQuestionComment); 해당 id가 있으면 update 없으면 insert
     //
    }
    
   /* @Override
    public void updateLikes(Long id) {
        QuestionComment questionComment = this.view(id);
        questionComment.setLikes(0L);
        questionCommentRepository.save(questionComment);
        System.out.println(questionComment.getLikes()+" 좋아요 누름");
    }*/

    @Transactional
    public void updateLikes(@Param("id") Long id) {
        questionCommentRepository.updateLikes(id);
    }

    @Transactional
    public void cancelLikes(@Param("id") Long id) {
        questionCommentRepository.cancelLikes(id);
    }
   /* @Override
    public void cancelLikes(Long id) {
        QuestionComment questionComment = this.view(id);
        questionComment.setLikes(0L);
        questionCommentRepository.save(questionComment);
        System.out.println(questionComment.getLikes()+" 좋아요 취소");
    }*/

    /*public void putQuestion(Long id, String question) {
        QuestionComment questionComment = this.view(id);
        String gptResponse = chatgptService.sendMessage(question);
       // System.out.println("지피티 답변 "+gptResponse);
        questionComment.setGptcontent(gptResponse);
        System.out.println("내답은? "+questionComment.getContent());
        System.out.println("데이터베이스에 잘 들어갔나요? "+questionComment.getGptcontent()+" 잘들어갔습니다!");
        //return chatgptService.sendMessage(question);
    }

    public void putQuestion2(Long id, String question) {
        QuestionComment questionComment = this.view(id);
        String gptResponse = chatgptService.sendMessage(question);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(gptResponse);
            if (jsonNode.isArray() && jsonNode.size() > 0) {
                String answer = jsonNode.get(0).asText();
                questionComment.setGptcontent(answer);
                questionCommentRepository.save(questionComment);
                System.out.println("내 답은? " + questionComment.getContent());
                System.out.println("데이터베이스에 잘 들어갔나요? " + questionComment.getGptcontent() + " 잘 들어갔습니다!");
            } else {
                System.out.println("유효한 응답이 아닙니다.");
            }
        } catch (JsonParseException e) {
            // JSON 파싱 오류 처리
            System.out.println("JSON 파싱 오류: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
        }
    }*/

    }
