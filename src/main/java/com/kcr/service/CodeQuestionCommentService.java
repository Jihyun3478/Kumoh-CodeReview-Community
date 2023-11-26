package com.kcr.service;

import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentRequestDTO;
import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.domain.dto.member.MsgResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import com.kcr.domain.entity.CodeQuestionComment;
import com.kcr.repository.CodeQuestionCommentRepository;
import com.kcr.repository.CodeQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeQuestionCommentService implements CommentService{

    @Autowired
    private CodeQuestionCommentRepository codeQuestionCommentRepository;
    @Autowired
    private CodeQuestionRepository codeQuestionRepository;

    //댓글 등록
    @Transactional
    public CodeQuestionCommentResponseDTO commentSave(Long id, CodeQuestionCommentRequestDTO codeQuestionCommentRequestDTO) {
        CodeQuestion codeQuestion = codeQuestionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        codeQuestionCommentRequestDTO.setCodeQuestion(codeQuestion);
        CodeQuestionComment codeQuestionComment = new CodeQuestionComment(codeQuestionCommentRequestDTO);
        CodeQuestionComment savedCodeQuestionComment = codeQuestionCommentRepository.save(codeQuestionComment);

        return new CodeQuestionCommentResponseDTO(savedCodeQuestionComment);
    }

    //댓글 수정
    @Transactional
    public void updateComment(Long codeQuestionId, Long commentId, CodeQuestionCommentRequestDTO requestDTO) {
        // 댓글 엔티티 조회
        CodeQuestionComment comment = codeQuestionCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 게시글 ID 일치 여부 검증
        if (!comment.getCodeQuestion().getId().equals(codeQuestionId)) {
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다: " + codeQuestionId);
        }

        // 댓글 업데이트
        comment.updateCodeQuestionComment(requestDTO.getContent(), requestDTO.getCodeContent());
    }

    //댓글삭제
    @Override
    public MsgResponseDTO delete(Long id) {
        codeQuestionCommentRepository.deleteById(id);
        return new MsgResponseDTO("게시글 삭제 완료", 200);
    }

    //대댓글등록
    @Transactional
    public CodeQuestionCommentResponseDTO saveChildComment(Long parentId, Long codeQuestionId, CodeQuestionCommentRequestDTO requestDTO) {
        String content = requestDTO.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("대댓글 내용이 비어있습니다.");
        }
        CodeQuestionComment parent = codeQuestionCommentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다: " + parentId));
        CodeQuestion codeQuestion = codeQuestionRepository.findById(codeQuestionId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " +codeQuestionId));
        requestDTO.setCodeQuestion(codeQuestion);
        CodeQuestionComment codeQuestionComment = new CodeQuestionComment(requestDTO);
        CodeQuestionComment child = codeQuestionCommentRepository.save(codeQuestionComment);
        child.updateParent(parent); // 대댓글에 부모 댓글 설정
        codeQuestionCommentRepository.save(child); //DB에 저장
        return new CodeQuestionCommentResponseDTO(child);
    }

    //댓글 조회(대댓글과 같이)
    @Transactional
    public List<CodeQuestionCommentResponseDTO> findAllChildComments(Long commentId) {
        return codeQuestionCommentRepository.findChildCommentsByParentId(commentId).stream()
                .map(CodeQuestionCommentResponseDTO::new)
                .collect(Collectors.toList());
    }
    //게시글의 코드를 불러옴
    @Transactional
    public String codeQuestionContent(Long codeQuesetionID){
        String codeQUestionContent = "";
        CodeQuestion codeQuestion = codeQuestionRepository.findById(codeQuesetionID).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + codeQuesetionID));

        codeQUestionContent = codeQuestion.getCodeContent();
        return codeQUestionContent;
    }
    @Transactional
    public Page<CodeQuestionCommentResponseDTO> findAllWithChild2(Long codeQuestionId, Pageable pageable) {
        Page<CodeQuestionComment> commentsPage = codeQuestionCommentRepository.findByCodeQuestionIdWithPagination(codeQuestionId,pageable);

        return commentsPage.map(CodeQuestionCommentResponseDTO::toCommentDTO);
    }
}
