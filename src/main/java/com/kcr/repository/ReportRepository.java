package com.kcr.repository;

import com.kcr.domain.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
//    Report save();
    Page<Report> findAll(Pageable pageable);
}