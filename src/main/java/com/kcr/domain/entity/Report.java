package com.kcr.domain.entity;

import com.kcr.domain.type.ReportType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id @GeneratedValue
    @Column(name = "REPORT_ID")
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Builder
    public Report(String content,ReportType reportType,String title){
        this.content = content;
        this.reportType = reportType;
        this.title = title;
    }
}
