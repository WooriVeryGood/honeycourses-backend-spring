package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDeleteResponse {

    private final Long postId;

    @Builder
    public PostDeleteResponse(Long postId) {
        this.postId = postId;
    }

}
