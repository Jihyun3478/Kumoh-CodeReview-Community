package com.kcr.service;

import com.kcr.domain.dto.report.ReportRequestDTO;
import com.kcr.domain.dto.report.ReportResponseDTO;
import com.kcr.domain.entity.Report;
import com.kcr.domain.type.ReportType;
import com.kcr.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    //작성자 신고등록
    @Transactional
    public Long saveWriterReport(ReportRequestDTO reportRequestDTO) {
        Report report = reportRequestDTO.toSaveEntity();
        report.setReportType(ReportType.WRITER_REPORT);
        reportRepository.save(report);
        return reportRequestDTO.getId();
    }

    //댓글 신고등록
    @Transactional
    public Long saveCommentReport(ReportRequestDTO reportRequestDTO) {
        Report report = reportRequestDTO.toSaveEntity();
        report.setReportType(ReportType.COMMENT_REPORT);
        reportRepository.save(report);
        return reportRequestDTO.getId();
    }

    //게시글 신고등록
    @Transactional
    public Long savePostReport(ReportRequestDTO reportRequestDTO) {

        Report report = reportRequestDTO.toSaveEntity();
        report.setReportType(ReportType.POST_REPORT);
        reportRepository.save(report);
        return reportRequestDTO.getId();
    }

    //관리자용 신고조회(신고목록조회)
    public List<ReportResponseDTO> findAllReport() {
        List<Report> reports = reportRepository.findAll();
        List<ReportResponseDTO> reportList = reports.stream()
                .map(ReportResponseDTO::toReportDTO)
                .collect(Collectors.toList());

        return reportList;

    }
}
