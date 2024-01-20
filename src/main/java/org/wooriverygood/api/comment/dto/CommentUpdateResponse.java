package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUpdateResponse {

    private final Long comment_id;

    @Builder
    public CommentUpdateResponse(Long comment_id) {
        this.comment_id = comment_id;
    }
}
