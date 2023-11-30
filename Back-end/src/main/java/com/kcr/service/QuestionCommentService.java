package com.kcr.service;

import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Question;
import com.kcr.domain.entity.QuestionComment;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestionCommentService implements CommentService {

    private final QuestionCommentRepository questionCommentRepository;

    private final QuestionRepository questionRepository;

    //댓글삭제
    @Override
    public MsgResponseDTO delete(Long id) {
        questionCommentRepository.deleteById(id);
        return new MsgResponseDTO("게시글 삭제 완료", 200);
    }

    @Transactional
    public QuestionComment view(Long id) {
        return questionCommentRepository.findById(id).get();
    }

    //댓글수정
    @Transactional
    public void update(Long id, QuestionCommentRequestDTO requestQuestionCommentDTO) {
        QuestionComment questionComment = questionCommentRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("수정에 실패했습니다.");
        });
        questionComment.updateQuestionComment(requestQuestionCommentDTO.getContent());
        System.out.println(questionComment.getContent() + " 수정된 content Service");
    }

    //댓글수정2
    @Transactional
    public void updateComment(Long questionId, Long commentId, QuestionCommentRequestDTO requestDTO) {
        // 댓글 엔티티 조회
        QuestionComment comment = questionCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 게시글 ID 일치 여부 검증
        if (!comment.getQuestion().getId().equals(questionId)) {
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다: " + questionId);
        }

        // 댓글 업데이트
        comment.updateQuestionComment(requestDTO.getContent());
    }

    @Transactional
    public List<QuestionCommentResponseDTO> findAll(Long questionId) {
        List<QuestionComment> questionCommentList = questionCommentRepository.findAllByQuestionId(questionId);
        /* Entity -> DTO */
        List<QuestionCommentResponseDTO> questionCommentDTOList = new ArrayList<>();
        for (QuestionComment questionComment : questionCommentList) {
            QuestionCommentResponseDTO questionCommentDTO = QuestionCommentResponseDTO.toCommentDTO(questionComment);
            questionCommentDTOList.add(questionCommentDTO);
        }
        return questionCommentDTOList;
    }

    @Transactional
    public QuestionCommentResponseDTO commentSave(Long id, QuestionCommentRequestDTO questionCommentRequestDTO) {
        Question question = questionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        questionCommentRequestDTO.setQuestion(question);
        QuestionComment questionComment = new QuestionComment(questionCommentRequestDTO);
        QuestionComment savedQuestionComment = questionCommentRepository.save(questionComment);

        return new QuestionCommentResponseDTO(savedQuestionComment);
    }

    @Transactional
    public List<QuestionCommentResponseDTO> findAllChildComments(Long commentId) {
        return questionCommentRepository.findChildCommentsByParentId(commentId).stream()
                .map(QuestionCommentResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Page<QuestionCommentResponseDTO> findAllWithChild2(Long questionId, Pageable pageable) {
        Page<QuestionComment> commentsPage = questionCommentRepository.findByQuestionIdWithPagination(questionId, pageable);

        return commentsPage.map(QuestionCommentResponseDTO::toCommentDTO);
    }

    @Transactional
    public QuestionCommentResponseDTO saveChildComment(Long parentId, Long questionID, QuestionCommentRequestDTO requestDTO) {
        String content = requestDTO.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("대댓글 내용이 비어있습니다.");
        }
        QuestionComment parent = questionCommentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다: " + parentId));
        Question question = questionRepository.findById(questionID)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " + questionID));
        requestDTO.setQuestion(question);
        QuestionComment questionComment = new QuestionComment(requestDTO);
        QuestionComment child = questionCommentRepository.save(questionComment);
        child.updateParent(parent); // 대댓글에 부모 댓글 설정
        questionCommentRepository.save(child); //DB에 저장
        return new QuestionCommentResponseDTO(child);
    }
}