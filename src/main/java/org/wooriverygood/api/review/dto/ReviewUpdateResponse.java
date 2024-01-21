package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewUpdateResponse {

    private final Long review_id;

    @Builder
    public ReviewUpdateResponse(Long review_id) {
        this.review_id = review_id;
    }
}
