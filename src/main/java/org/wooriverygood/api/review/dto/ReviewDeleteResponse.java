package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewDeleteResponse {
    private final Long review_id;

    @Builder
    public ReviewDeleteResponse(Long review_id) {
        this.review_id = review_id;
    }
}
