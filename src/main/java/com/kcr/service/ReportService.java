package com.kcr.service;

import com.kcr.domain.dto.report.PostReportRequestDTO;
import com.kcr.domain.dto.report.WriterReportRequestDTO;
import com.kcr.domain.entity.Report;
import com.kcr.domain.type.ReportType;
import com.kcr.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;


    // 작성자 신고
    @Transactional
    public Long saveWriterReport(WriterReportRequestDTO requestDTO) {
        String writer = requestDTO.getWriter();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();
        Report report = new Report(writer, title, content, ReportType.WRITER_REPORT);
        reportRepository.save(report);

        return report.getId();
    }

    // Q&A 게시글 신고
    @Transactional
    public Long saveQuestionReport(PostReportRequestDTO requestDTO) {
        Long postId = requestDTO.getPostId();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();
        Report report = new Report(postId, title, content, ReportType.QUESTION_REPORT);
        reportRepository.save(report);

        return report.getId();
    }

    // Q&A 게시글 댓글 신고
    @Transactional
    public Long saveQuestionCommentReport(WriterReportRequestDTO requestDTO) {
        String writer = requestDTO.getWriter();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();
        Report report = new Report(writer, title, content, ReportType.QUESTION_COMMENT_REPORT);
        reportRepository.save(report);

        return report.getId();
    }

    // 코드리뷰 게시글 신고
    @Transactional
    public Long saveCodequestionReport(PostReportRequestDTO requestDTO) {
        Long postId = requestDTO.getPostId();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();
        Report report = new Report(postId, title, content, ReportType.CODE_QUESTION_REPORT);
        reportRepository.save(report);

        return report.getId();
    }

    // 코드리뷰 게시글 댓글 신고
    @Transactional
    public Long saveCodequestionCommentReport(WriterReportRequestDTO requestDTO) {
        String writer = requestDTO.getWriter();
        String title = requestDTO.getTitle();
        String content = requestDTO.getContent();
        Report report = new Report(writer, title, content, ReportType.CODE_QUESTION_COMMENT_REPORT);
        reportRepository.save(report);

        return report.getId();
    }

    //관리자용 신고조회(신고목록조회)
    public Page<Report> findAllReport(Pageable pageable) {
        return reportRepository.findAll(pageable);
    }
}
