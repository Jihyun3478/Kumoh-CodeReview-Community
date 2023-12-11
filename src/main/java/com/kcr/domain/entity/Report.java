package com.kcr.domain.entity;

import com.kcr.domain.type.ReportType;
import com.kcr.domain.type.RoleType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@Table(name = "report")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "REPORT_ID")
    private Long id;

    private Long postId;

    private String writer;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    public Report(String writer, String title, String content, ReportType reportType) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.reportType = reportType;
    }

    public Report(Long postId, String title, String content, ReportType reportType) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.reportType = reportType;
    }
}
