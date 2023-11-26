package com.kcr.service;

import com.kcr.domain.dto.member.MemberListResponseDTO;
import com.kcr.domain.dto.report.ReportResponseDTO;
import com.kcr.domain.entity.Member;
import com.kcr.domain.entity.Report;
import com.kcr.domain.type.RoleType;
import com.kcr.repository.MemberRepository;
import com.kcr.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public Page<MemberListResponseDTO> findAllMember(Pageable pageable) {
        RoleType roleType = RoleType.USER;
        Page<Member> page = memberRepository.findAll(pageable, roleType);
        return page.map(MemberListResponseDTO::new);
    }

    @Transactional
    public Page<ReportResponseDTO> findAllReport(Pageable pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        return page.map(ReportResponseDTO::new);
    }
}
