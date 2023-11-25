package com.kcr.domain.dto.report;

import com.kcr.domain.entity.Report;
import com.kcr.domain.type.ReportType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReportRequestDTO {
    private Long id;
    private String content;
    private ReportType reportType;
    private String title;

    @Builder
    public ReportRequestDTO(String content,ReportType reportType,String title) {
        this.content = content;
        this.reportType = reportType;
        this.title = title;
    }
    /* DTO -> Entity */
    public Report toSaveEntity() {
        return Report.builder()
                .content(content)
                .reportType(reportType)
                .title(title)
                .build();
    }
}
