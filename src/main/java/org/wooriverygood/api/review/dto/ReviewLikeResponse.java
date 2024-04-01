package org.wooriverygood.api.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewLikeResponse {

    private final int likeCount;

    private final boolean liked;


    @Builder
    public ReviewLikeResponse(int likeCount, boolean liked) {
        this.likeCount = likeCount;
        this.liked =liked;
    }

}
