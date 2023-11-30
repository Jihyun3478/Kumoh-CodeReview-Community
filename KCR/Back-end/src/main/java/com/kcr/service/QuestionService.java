package com.kcr.service;

import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.entity.ChatGPT;
import com.kcr.domain.entity.Question;
import com.kcr.repository.ChatGPTRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ChatGPTRepository chatGPTRepository;
    private final ChatGptService chatGptService;

    /* 게시글 등록 */
    @Transactional // 메소드 실행 시 트랜잭션 시작 -> 정상 종료되면 커밋 / 에러 시 롤백
    public QuestionResponseDTO save(QuestionRequestDTO requestDTO) {
        Question question = new Question(requestDTO);
        Question savedQuestion = questionRepository.save(question);

        ChatGptResponse chatGptResponse = null;
        chatGptResponse= chatGptService.askQuestion2(savedQuestion);
        ChatGPT chatGPT =  chatGptResponse.toSaveEntity();
        System.out.println("chatgpt data : "+chatGPT.getId()+" "+chatGPT.getGptContent()+" "+chatGPT.getQuestion().getId());
        chatGPTRepository.save(chatGPT);

        return new QuestionResponseDTO(savedQuestion);
    }

    /* 게시글 수정 */
    @Transactional
    public QuestionResponseDTO update(Long id, QuestionRequestDTO requestDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        question.updateQuestion(requestDTO.getTitle(), requestDTO.getContent());

        ChatGPT chatGPT = chatGPTRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        ChatGPT saveEntity = chatGptService.askQuestion2(question).toSaveEntity();
        chatGPTRepository.updateContent(chatGPT.getId(), saveEntity.getGptContent());


        return new QuestionResponseDTO(question);
    }

    /* 게시글 삭제 */
    @Transactional
    public MsgResponseDTO delete(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        questionRepository.delete(question);
        return new MsgResponseDTO("게시글 삭제 완료", 200);
    }

    /* 게시글 전체 조회 */
    @Transactional
    public Page<QuestionListResponseDTO> findAll(Pageable pageable) {
        Page<Question> page = questionRepository.findAll(pageable);
        return page.map(QuestionListResponseDTO::new);
    }

    /* 게시글 상세 조회 */
    @Transactional
    public QuestionResponseDTO findById(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return QuestionResponseDTO.builder()
                .id(question.getId())
                .title(question.getTitle())
                .writer(question.getWriter())
                .content(question.getContent())
                .createDate(question.getCreateDate())
                .totalLikes(question.getTotalLikes())
                .views(question.getViews())
                .build();
    }

    /* 게시글 제목 검색 */
    @Transactional
    public Page<QuestionListResponseDTO> searchByTitle(String keyword, Pageable pageable) {
        return questionRepository.findByTitleContaining(keyword, pageable);
    }

    /* 게시글 작성자 검색 */
    @Transactional
    public Page<QuestionListResponseDTO> searchByWriter(String keyword, Pageable pageable) {
        return questionRepository.findByWriterContaining(keyword, pageable);
    }

    /* 게시글 조회수 업데이트 */
    @Transactional
    public void updateViews(@Param("id") Long id) {
        questionRepository.updateViews(id);
    }
}
