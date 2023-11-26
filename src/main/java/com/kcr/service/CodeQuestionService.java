package com.kcr.service;

import com.kcr.domain.dto.codequestion.CodeQuestionListResponseDTO;
import com.kcr.domain.dto.codequestion.CodeQuestionRequestDTO;
import com.kcr.domain.dto.codequestion.CodeQuestionResponseDTO;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Question;
import com.kcr.domain.exception.CustomException;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.CodeQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.kcr.domain.exception.ErrorCode.NOT_FOUND_BOARD;
import static com.kcr.domain.exception.ErrorCode.NOT_FOUND_BOARD_OR_AUTHORIZATION;

@Service
@RequiredArgsConstructor // 생성자 의존관계 주입
public class CodeQuestionService {

    private final CodeQuestionRepository codeQuestionRepository;

    /* 게시글 등록 */
    @Transactional // 메소드 실행 시 트랜잭션 시작 -> 정상 종료되면 커밋 / 에러 시 롤백
    public CodeQuestionResponseDTO save(CodeQuestionRequestDTO requestDTO) {
        CodeQuestion codeQuestion = new CodeQuestion(requestDTO);
        CodeQuestion savedCodeQuestion = codeQuestionRepository.save(codeQuestion);

        return new CodeQuestionResponseDTO(savedCodeQuestion);
    }

    /* 게시글 수정 */
    @Transactional
    public CodeQuestionResponseDTO update(Long id, CodeQuestionRequestDTO requestDTO) {
        CodeQuestion codeQuestion = codeQuestionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        codeQuestion.updateCodeQuestion(requestDTO.getTitle(), requestDTO.getContent(), requestDTO.getCodeContent());
        return new CodeQuestionResponseDTO(codeQuestion);
    }

    /* 게시글 삭제 */
    @Transactional
    public MsgResponseDTO delete(Long id) {
        CodeQuestion codeQuestion = codeQuestionRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        codeQuestionRepository.delete(codeQuestion);
        return new MsgResponseDTO("게시글 삭제 완료", 200);
    }

    /* 게시글 전체 조회 */
    @Transactional
    public Page<CodeQuestionListResponseDTO> findAll(Pageable pageable) {
        Page<CodeQuestion> page = codeQuestionRepository.findAll(pageable);
        return page.map(CodeQuestionListResponseDTO::new);
    }

    /* 게시글 상세 조회 */
    @Transactional
    public CodeQuestionResponseDTO findById(Long id) {
        CodeQuestion codeQuestion = codeQuestionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        return CodeQuestionResponseDTO.builder()
                .id(codeQuestion.getId())
                .title(codeQuestion.getTitle())
                .writer(codeQuestion.getWriter())
                .content(codeQuestion.getContent())
                .codeContent(codeQuestion.getCodeContent())
                .createDate(codeQuestion.getCreateDate())
                .totalLikes(codeQuestion.getTotalLikes())
                .views(codeQuestion.getViews())
                .build();
    }

    /* 게시글 제목 검색 */
    @Transactional
    public Page<CodeQuestionListResponseDTO> searchByTitle(String keyword, Pageable pageable) {
        return codeQuestionRepository.findByTitleContaining(keyword, pageable);
    }

    /* 게시글 작성자 검색 */
    @Transactional
    public Page<CodeQuestionListResponseDTO> searchByWriter(String keyword, Pageable pageable) {
        return codeQuestionRepository.findByWriterContaining(keyword, pageable);
    }

    /* 게시글 조회수 업데이트 */
    @Transactional
    public void updateViews(@Param("id") Long id) {
        codeQuestionRepository.updateViews(id);
    }
}
