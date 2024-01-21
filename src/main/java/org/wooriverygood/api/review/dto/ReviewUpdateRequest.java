package org.wooriverygood.api.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {
    @NotBlank(message = "제목이 없습니다.")
    private String review_title;

    private String review_content;

    @Builder
    public ReviewUpdateRequest(String review_title, String review_content) {
        this.review_title = review_title;
        this.review_content = review_content;
    }
}
