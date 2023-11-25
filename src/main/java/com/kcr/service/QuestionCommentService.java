package com.kcr.service;

import com.kcr.domain.dto.questioncomment.QuestionCommentRequestDTO;
import com.kcr.domain.dto.questioncomment.QuestionCommentResponseDTO;
import com.kcr.domain.entity.Question;
import com.kcr.domain.entity.QuestionComment;
import com.kcr.repository.QuestionCommentRepository;
import com.kcr.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestionCommentService implements CommentService {
    @Autowired
    private QuestionCommentRepository questionCommentRepository;
    @Autowired
    private QuestionRepository questionRepository;

    //댓글삭제
    @Override
    public void delete(Long id) {
        questionCommentRepository.deleteById(id);
    }

    public QuestionComment view(Long id) {
        return questionCommentRepository.findById(id).get();
    }

    //댓글수정
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
    public Long commentSave(Long id, QuestionCommentRequestDTO questionCommentRequestDTO) {
        Question question = questionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        questionCommentRequestDTO.setQuestion(question);
        QuestionComment questionComment = questionCommentRequestDTO.toSaveEntity();
       // questionComment.updateParent(questionComment);
        questionCommentRepository.save(questionComment);
        return questionCommentRequestDTO.getId();
    }

    public List<QuestionCommentResponseDTO> findAllChildComments(Long commentId) {
        return questionCommentRepository.findChildCommentsByParentId(commentId).stream()
                .map(QuestionCommentResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<QuestionCommentResponseDTO> findAllWithChild2(Long questionId, int page) {
        int size = 5; // 페이지당 댓글 수
        int limit = size;
        int offset = (page - 1) * size;

        List<QuestionComment> comments = questionCommentRepository.findAllWithRepliesByQuestionId2(questionId, limit, offset);

        List<QuestionCommentResponseDTO> questionCommentDTOList = new ArrayList<>();
        for (QuestionComment questionComment : comments) {
            QuestionCommentResponseDTO dto = QuestionCommentResponseDTO.toCommentDTO(questionComment);
            questionCommentDTOList.add(dto);
        }

        return questionCommentDTOList;
    }

    @Transactional
    public Long saveChildComment(Long parentId, Long questionID, QuestionCommentRequestDTO requestDTO) {
        String content = requestDTO.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("대댓글 내용이 비어있습니다.");
        }
        QuestionComment parent = questionCommentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다: " + parentId));
        Question question = questionRepository.findById(questionID)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " +questionID));
        requestDTO.setQuestion(question);
//        requestDTO.setParentComment(parent);
       QuestionComment child = requestDTO.toSaveEntity();
        child.updateParent(parent); // 대댓글에 부모 댓글 설정
        //  parent.setChild((List<QuestionComment>) child);
        questionCommentRepository.save(child); //DB에 저장
        return child.getId();
    }
}