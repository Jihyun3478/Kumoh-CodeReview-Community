package com.kcr.controller;

import com.kcr.domain.dto.member.MemberListResponseDTO;
import com.kcr.domain.dto.report.ReportResponseDTO;
import com.kcr.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/memberList")
    public ResponseEntity<Page<MemberListResponseDTO>> findAllMember(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<MemberListResponseDTO> responseDTOS = adminService.findAllMember(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }

    @GetMapping("/admin/reportList")
    public ResponseEntity<Page<ReportResponseDTO>> findAllReport(@PageableDefault(sort = "createDate", direction = Sort.Direction.DESC, size = 15) Pageable pageable) {
        Page<ReportResponseDTO> responseDTOS = adminService.findAllReport(pageable);
        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }
}
