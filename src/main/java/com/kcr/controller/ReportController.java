package com.kcr.controller;

import com.kcr.domain.dto.question.QuestionListResponseDTO;
import com.kcr.domain.dto.report.PostReportRequestDTO;
import com.kcr.domain.dto.report.ReportResponseDTO;
import com.kcr.domain.dto.report.WriterReportRequestDTO;
import com.kcr.domain.entity.Report;
import com.kcr.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // 작성자 신고
    @PostMapping("/reportByWriter") // 작성자 닉네임
    public ResponseEntity<Long> saveReportByWriter(@RequestBody WriterReportRequestDTO requestDTO) {
        return new ResponseEntity<>(reportService.saveWriterReport(requestDTO), HttpStatus.OK);
    }

    // Q&A 게시글 신고
    @PostMapping("/reportByQuestion") // 게시글 아이디
    public ResponseEntity<Long> saveReportByQuestion(@RequestBody PostReportRequestDTO requestDTO) {
        return new ResponseEntity<>(reportService.saveQuestionReport(requestDTO), HttpStatus.OK);
    }

    // Q&A 게시글 댓글 신고
    @PostMapping("/reportByQuestionComment") // 댓글 아이디
    public ResponseEntity<Long> saveReportByQuestionComment(@RequestBody WriterReportRequestDTO requestDTO) {
        return new ResponseEntity<>(reportService.saveQuestionCommentReport(requestDTO), HttpStatus.OK);
    }

    // 코드리뷰 게시글 신고
    @PostMapping("/reportByCodequestion") // 게시글 아이디
    public ResponseEntity<Long> saveReportByCodequestion(@RequestBody PostReportRequestDTO requestDTO) {
        return new ResponseEntity<>(reportService.saveCodequestionReport(requestDTO), HttpStatus.OK);
    }

    // 코드리뷰 게시글 댓글 신고
    @PostMapping("/reportByCodequestionComment") // 댓글 아이디
    public ResponseEntity<Long> saveReportByCodequestionComment(@RequestBody WriterReportRequestDTO requestDTO) {
        return new ResponseEntity<>(reportService.saveCodequestionCommentReport(requestDTO), HttpStatus.OK);
    }

    // 신고 전체 조회(관리자)
    @GetMapping("/reportAll")
    public ResponseEntity<Page<Report>> findAll(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        Page<Report> responseDTOS = reportService.findAllReport(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }
}

//package com.kcr.controller;
//
//import com.kcr.domain.dto.report.PostReportRequestDTO;
//import com.kcr.domain.dto.report.WriterReportRequestDTO;
//import com.kcr.domain.entity.Report;
//import com.kcr.service.ReportService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@Controller
//@RequiredArgsConstructor
//public class ReportController {
//
//    private final ReportService reportService;
//
//    // 작성자 신고
//    @PostMapping("/reportByWriter") // 작성자 닉네임
//    public ResponseEntity<Long> saveReportByWriter(@RequestBody WriterReportRequestDTO requestDTO) {
//        return new ResponseEntity<>(reportService.saveWriterReport(requestDTO), HttpStatus.OK);
//    }
//
//    // Q&A 게시글 신고
//    @PostMapping("/reportByQuestion") // 게시글 아이디
//    public ResponseEntity<Long> saveReportByQuestion(@RequestBody PostReportRequestDTO requestDTO) {
//        return new ResponseEntity<>(reportService.saveQuestionReport(requestDTO), HttpStatus.OK);
//    }
//
//    // Q&A 게시글 댓글 신고
//    @PostMapping("/reportByQuestionComment") // 댓글 아이디
//    public ResponseEntity<Long> saveReportByQuestionComment(@RequestBody WriterReportRequestDTO requestDTO) {
//        return new ResponseEntity<>(reportService.saveQuestionCommentReport(requestDTO), HttpStatus.OK);
//    }
//
//    // 코드리뷰 게시글 신고
//    @PostMapping("/reportByCodequestion") // 게시글 아이디
//    public ResponseEntity<Long> saveReportByCodequestion(@RequestBody PostReportRequestDTO requestDTO) {
//        return new ResponseEntity<>(reportService.saveCodequestionReport(requestDTO), HttpStatus.OK);
//    }
//
//    // 코드리뷰 게시글 댓글 신고
//    @PostMapping("/reportByCodequestionComment") // 댓글 아이디
//    public ResponseEntity<Long> saveReportByCodequestionComment(@RequestBody WriterReportRequestDTO requestDTO) {
//        return new ResponseEntity<>(reportService.saveCodequestionCommentReport(requestDTO), HttpStatus.OK);
//    }
//
//    // 신고 전체 조회(관리자)
//    @GetMapping("/reportAll")
//    public ResponseEntity<Page<Report>> findAll(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
//        Page<Report> responseDTOS = reportService.findAllReport(pageable);
//        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
//    }
//}
