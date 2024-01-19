package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentLikeResponse {

    private final int like_count;

    private final boolean liked;

    @Builder
    public CommentLikeResponse(int like_count, boolean liked) {
        this.like_count = like_count;
        this.liked = liked;
    }

}
