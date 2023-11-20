package com.kcr.service;

import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.question.QuestionRequestDTO;
import com.kcr.domain.dto.question.QuestionResponseDTO;
import com.kcr.domain.entity.Question;
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

    /* 게시글 등록 */
    @Transactional // 메소드 실행 시 트랜잭션 시작 -> 정상 종료되면 커밋 / 에러 시 롤백
    public Long save(QuestionRequestDTO requestDTO) {
        return questionRepository.save(requestDTO.toSaveEntity()).getId();
    }

    /* 게시글 수정 */
    @Transactional
    public Long update(Long id, QuestionRequestDTO requestDTO) {
        Question question = questionRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        question.updateQuestion(requestDTO.getTitle(), requestDTO.getContent());
        return id;
    }

    /* 게시글 삭제 */
    @Transactional
    public void delete(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        questionRepository.delete(question);
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
                .likes(question.getLikes())
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

    /* 게시글 공감수 업데이트 */
    @Transactional
    public void updateLikes(@Param("id") Long id) {
        questionRepository.updateLikes(id);
    }
}
