package com.kcr.service;

import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentRequestDTO;
import com.kcr.domain.dto.codequestioncomment.CodeQuestionCommentResponseDTO;
import com.kcr.domain.entity.CodeQuestion;
import com.kcr.domain.entity.CodeQuestionComment;
import com.kcr.repository.CodeQuestionCommentRepository;
import com.kcr.repository.CodeQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CodeQuestionCommentService implements CommentService{

    @Autowired
    private CodeQuestionCommentRepository codeQuestionCommentRepository;
    @Autowired
    private CodeQuestionRepository codeQuestionRepository;
    //댓글 수정
    @Transactional
    public void updateComment(Long codequestionid, Long id, CodeQuestionCommentRequestDTO requestDTO) {
        CodeQuestionComment comment = codeQuestionCommentRepository.findById(codequestionid)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + codequestionid));

        // 게시글 ID 일치 여부 검증
        if (!comment.getCodeQuestion().getId().equals(codequestionid)) {
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다: " + codequestionid);
        }

        // 댓글 업데이트
        comment.updateCodeQuestionComment(requestDTO.getContent());
    }
    //댓글삭제
    @Override
    public void delete(Long id) {
        codeQuestionCommentRepository.deleteById(id);
    }

    //댓글 등록
    @Transactional
    public Long commentSave(Long id, CodeQuestionCommentRequestDTO codeQuestionCommentRequestDTO) {
        String content = codeQuestionCommentRequestDTO.getContent();
        String codeContent = codeQuestionCommentRequestDTO.getCodeContent();
        if (content == null || content.trim().isEmpty()) {
            System.out.println("댓글 내용 없음");
            throw new IllegalArgumentException("댓글 내용이 비어있습니다.");
        }
        else if(codeContent == null || codeContent.trim().isEmpty()){
            System.out.println("코드 박스 내용 없음");
            throw new IllegalArgumentException("코드 박스 내용이 비어있습니다.");
        }
        CodeQuestion codeQuestion = codeQuestionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        codeQuestionCommentRequestDTO.setCodeQuestion(codeQuestion);
        CodeQuestionComment codeQuestionComment = codeQuestionCommentRequestDTO.toSaveEntity();
        codeQuestionComment.updateParent(codeQuestionComment);
        codeQuestionCommentRepository.save(codeQuestionComment);

        System.out.println("commentsave함수에서 처리한것 "+codeQuestionCommentRequestDTO.getId());
        return codeQuestionCommentRequestDTO.getId();
    }

    //대댓글 등록
    public Long saveChildComment(Long parentId, Long codeQuestionID, CodeQuestionCommentRequestDTO requestDTO) {
        String content = requestDTO.getContent();
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("대댓글 내용이 비어있습니다.");
        }
        CodeQuestionComment parent = codeQuestionCommentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다: " + parentId));
        CodeQuestion codeQuestion = codeQuestionRepository.findById(codeQuestionID)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다: " +codeQuestionID));
        requestDTO.setCodeQuestion(codeQuestion);
//        requestDTO.setParentComment(parent);
        CodeQuestionComment child = requestDTO.toSaveEntity();
        child.updateParent(parent); // 대댓글에 부모 댓글 설정
        //  parent.setChild((List<QuestionComment>) child);
        codeQuestionCommentRepository.save(child); //DB에 저장

        return child.getId();
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
    public List<CodeQuestionCommentResponseDTO> findAllWithChild2(Long codeQuestionId, int page) {
        int size = 5; // 페이지당 댓글 수
        int limit = size;
        int offset = (page - 1) * size;
        System.out.println("codequestionID :"+ codeQuestionId);
        List<CodeQuestionComment> comments = codeQuestionCommentRepository.findAllWithRepliesByCodeQuestionId(codeQuestionId, limit, offset);

        List<CodeQuestionCommentResponseDTO> codeQuestionCommentResponseDTOList = new ArrayList<>();
        for (CodeQuestionComment codeQuestionComment : comments) {
            CodeQuestionCommentResponseDTO dto = CodeQuestionCommentResponseDTO.toCommentDTO(codeQuestionComment);
            codeQuestionCommentResponseDTOList.add(dto);
        }
        System.out.println("comments size :"+comments.size());
        for(int i = 0;i<comments.size();i++){
            System.out.println("comments  :"+comments.get(i).getCodeContent());
        }
        return codeQuestionCommentResponseDTOList;
    }
}
