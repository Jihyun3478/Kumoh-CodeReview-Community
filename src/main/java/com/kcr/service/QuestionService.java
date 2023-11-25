package com.kcr.service;

import com.kcr.domain.dto.chatGPT.ChatGptResponse;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.entity.ChatGPT;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Question;
import com.kcr.domain.exception.CustomException;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.ChatGPTRepository;
import com.kcr.repository.MemberRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kcr.domain.exception.ErrorCode.NOT_FOUND_BOARD;
import static com.kcr.domain.exception.ErrorCode.NOT_FOUND_BOARD_OR_AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final ChatGPTRepository chatGPTRepository;
    private final ChatGptService chatGptService;

    /* 게시글 등록 */
    @Transactional // 메소드 실행 시 트랜잭션 시작 -> 정상 종료되면 커밋 / 에러 시 롤백
    public QuestionResponseDTO save(QuestionRequestDTO requestDTO, Member member) { //
        Question question = new Question(requestDTO, member); //
        Question savedQuestion = questionRepository.save(question);

        ChatGptResponse chatGptResponse = null;
        chatGptResponse= chatGptService.askQuestion2(question,chatGptResponse);
        ChatGPT chatGPT =  chatGptResponse.toSaveEntity();
        System.out.println("chatgpt data : "+chatGPT.getId()+" "+chatGPT.getGptContent()+" "+chatGPT.getQuestion().getId());
        chatGPTRepository.save(chatGPT);

        return new QuestionResponseDTO(question);
    }

    /* 게시글 수정 */
    @Transactional
    public QuestionResponseDTO update(Long id, QuestionRequestDTO requestDTO, Member member) {
        Question question = findByQuestionIdAndUser(id, member);
        question.updateQuestion(requestDTO.getTitle(), requestDTO.getContent());

        return new QuestionResponseDTO(question);
    }
//    @Transactional
//    public Long update(Long id, QuestionRequestDTO requestDTO, Member member) {
//        Question question = questionRepository.findById(id)
//                .orElseThrow(IllegalArgumentException::new);
//        question.updateQuestion(requestDTO.getTitle(), requestDTO.getContent());
//        return id;
//    }

    /* 게시글 삭제 */
    @Transactional
    public MsgResponseDTO delete(Long id, Member member) {
        Question question = findByQuestionIdAndUser(id, member);
        questionRepository.delete(question);

        return new MsgResponseDTO("게시글을 삭제했습니다.", HttpStatus.OK.value());
    }
//    @Transactional
//    public void delete(Long id, Member member) {
//        Question question = questionRepository.findById(id)
//                .orElseThrow(RuntimeException::new);
//
//        questionRepository.delete(question);
//    }

    // 사용자의 권한 확인 - 게시글
    Question findByQuestionIdAndUser(Long questionId, Member member) {
        Question question;

        // ADMIN
        if (member.getRoleType().equals(RoleType.ADMIN)) {
            question = questionRepository.findById(questionId).orElseThrow(
                    () -> new CustomException(NOT_FOUND_BOARD)
            );
            // USER
        } else {
            question = questionRepository.findByIdAndMemberId(questionId, member.getId()).orElseThrow (
                    () -> new CustomException(NOT_FOUND_BOARD_OR_AUTHORIZATION)
            );
        }

        return question;
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
