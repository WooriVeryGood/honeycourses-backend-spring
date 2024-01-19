package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDeleteResponse {

    private final Long post_id;

    @Builder
    public PostDeleteResponse(Long post_id) {
        this.post_id = post_id;
    }

}
