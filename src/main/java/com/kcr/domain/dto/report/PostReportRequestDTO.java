package com.kcr.domain.dto.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostReportRequestDTO {
    private Long postId;
    private String title;
    private String content;
}
