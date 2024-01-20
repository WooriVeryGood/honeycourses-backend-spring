package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateResponse {

    private final Long post_id;


    @Builder
    public PostUpdateResponse(Long post_id) {
        this.post_id = post_id;
    }
}
