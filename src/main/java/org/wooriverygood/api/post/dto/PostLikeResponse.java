package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostLikeResponse {

    private final int likeCount;

    private final boolean liked;


    @Builder
    public PostLikeResponse(int likeCount, boolean liked) {
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
