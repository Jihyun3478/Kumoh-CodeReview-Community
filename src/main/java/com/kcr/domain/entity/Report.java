package com.kcr.domain.entity;

import com.kcr.domain.type.ReportType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
