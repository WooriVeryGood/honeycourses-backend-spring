package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDeleteResponse {

    private final Long comment_id;

    @Builder
    public CommentDeleteResponse(Long comment_id) {
        this.comment_id = comment_id;
    }
}
