package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeResponse {

    private final int like_count;

    private final boolean liked;


    @Builder
    public PostLikeResponse(int like_count, boolean liked) {
        this.like_count = like_count;
        this.liked = liked;
    }
}
