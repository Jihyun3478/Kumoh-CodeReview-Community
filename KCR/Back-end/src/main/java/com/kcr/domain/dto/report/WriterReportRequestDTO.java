package com.kcr.domain.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WriterReportRequestDTO {
    private String writer;
    private String title;
    private String content;
}
