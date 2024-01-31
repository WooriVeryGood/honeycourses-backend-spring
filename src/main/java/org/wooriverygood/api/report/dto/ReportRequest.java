package org.wooriverygood.api.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportRequest {

    @NotBlank(message = "신고내용은 1자 이상 255자 이하여야 합니다.")
    private String message;

    @Builder
    public ReportRequest(String message) {
        this.message = message;
    }
}
