package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeResponse {

    private final int likeCount;

    private final boolean liked;


    @Builder
    public PostLikeResponse(int likeCount, boolean liked) {
        this.likeCount = likeCount;
        this.liked = liked;
    }
}
