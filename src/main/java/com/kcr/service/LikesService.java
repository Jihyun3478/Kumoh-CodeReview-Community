package com.kcr.service;

import com.kcr.domain.dto.likes.LikesRequestDTO;
import com.kcr.domain.dto.likes.LikesResponseDTO;
import com.kcr.domain.entity.*;
import com.kcr.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final QuestionRepository questionRepository;
    private final CodeQuestionRepository codeQuestionRepository;
    private final QuestionCommentRepository questionCommentRepository;
    private final CodeQuestionCommentRepository codeQuestionCommentRepository;


    //q&a게시글 공감
    @Transactional
    public void questionLikes(Long questionID, LikesRequestDTO likesRequestDTO){
        Question question = questionRepository.findById(questionID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패: 해당 게시글이 존재하지 않습니다." + questionID));
        likesRequestDTO.setQuestion(question);
        Likes likes = likesRequestDTO.toSaveEntity();
        likes.updateQuestion(question);

        question.setTotalLikes(question.getTotalLikes() + 1);
        questionRepository.save(question);
        likesRepository.save(likes);
    }
    //q&a 게시글 공감 취소
    @Transactional
    public void cancelQuestionLikes(Long questionID){
        Question question = questionRepository.findById(questionID).orElseThrow(() ->
                new IllegalArgumentException("게시글이 존재하지 않습니다: " + questionID));
        likesRepository.deleteByQuestionId(question);

        // 총 공감 수 감소
        question.setTotalLikes(question.getTotalLikes() - 1);
        questionRepository.save(question);
    }

    //codeQuestion 게시글 공감
    @Transactional
    public void codeQuestionLikes(Long questionID, LikesRequestDTO likesRequestDTO){
        CodeQuestion codeQuestion = codeQuestionRepository.findById(questionID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패: 해당 게시글이 존재하지 않습니다." + questionID));
        likesRequestDTO.setCodeQuestion(codeQuestion);
        Likes likes = likesRequestDTO.toSaveEntity();
        likes.updateCodeQuestion(codeQuestion);

        codeQuestion.setTotalLikes(codeQuestion.getTotalLikes()+1);
        codeQuestionRepository.save(codeQuestion);
        likesRepository.save(likes);
    }
    //codeQuestion 게시글 공감 취소
    @Transactional
    public void cancelCodeQuestionLikes(Long questionID){
        CodeQuestion codeQuestion = codeQuestionRepository.findById(questionID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패: 해당 게시글이 존재하지 않습니다." + questionID));
        likesRepository.deleteByCodeQuestionId(codeQuestion);

        // 총 공감 수 감소
        codeQuestion.setTotalLikes(codeQuestion.getTotalLikes()-1);
        codeQuestionRepository.save(codeQuestion);
    }

    //q&a댓글 공감
    @Transactional
    public void questionCommentLikes(Long commentID, LikesRequestDTO likesRequestDTO){
        QuestionComment questionComment = questionCommentRepository.findById(commentID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패 : 해당 댓글이 존재하지 않습니다: " + commentID));

        likesRequestDTO.setQuestionComment(questionComment);
        Likes likes = likesRequestDTO.toSaveEntity();
        likes.updateQuestionComment(questionComment);

       questionComment.setTotalLikes(questionComment.getTotalLikes()+1);
       questionCommentRepository.save(questionComment);
        likesRepository.save(likes);
    }

    //q&a댓글 공감 취소
    @Transactional
    public void cancelquestionCommentLikes(Long commentID){
        QuestionComment questionComment = questionCommentRepository.findById(commentID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패 : 해당 댓글이 존재하지 않습니다: " + commentID));
        likesRepository.deleteByQuestionCommentId(questionComment);

        // 총 공감 수 감소
        questionComment.setTotalLikes(questionComment.getTotalLikes()-1);
        questionCommentRepository.save(questionComment);
    }

    //codeQuestion 댓글 공감
    @Transactional
    public void codeQuestionCommentLikes(Long commentID, LikesRequestDTO likesRequestDTO){
        CodeQuestionComment codeQuestionComment = codeQuestionCommentRepository.findById(commentID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패 : 해당 댓글이 존재하지 않습니다: " + commentID));

        likesRequestDTO.setCodeQuestionComment(codeQuestionComment);
        Likes likes = likesRequestDTO.toSaveEntity();
        likes.updateCodeQuestionComment(codeQuestionComment);

        codeQuestionComment.setTotalLikes(codeQuestionComment.getTotalLikes()+1);
        codeQuestionCommentRepository.save(codeQuestionComment);
        likesRepository.save(likes);
    }

    //codeQuestion 댓글 공감 취소
    @Transactional
    public void cancelCodeQuestionCommentLikes(Long commentID){
        CodeQuestionComment codeQuestionComment = codeQuestionCommentRepository.findById(commentID).orElseThrow(() ->
                new IllegalArgumentException("공감 실패 : 해당 댓글이 존재하지 않습니다: " + commentID));
        likesRepository.deleteByCodeQuestionCommentId(codeQuestionComment);

        // 총 공감 수 감소
        codeQuestionComment.setTotalLikes(codeQuestionComment.getTotalLikes()-1);
        codeQuestionCommentRepository.save(codeQuestionComment);
    }
    @Transactional
    public List<LikesResponseDTO> findAllByQuestion(Long questionID) {
        return likesRepository.findAllWithQuestion(questionID).stream()
                .map(LikesResponseDTO::new)
                .collect(Collectors.toList());
    }

}
