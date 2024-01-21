package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewLikeResponse {
    private final int like_count;
    private final boolean liked;

    @Builder
    public ReviewLikeResponse(int like_count, boolean liked) {
        this.like_count = like_count;
        this.liked =liked;
    }
}
