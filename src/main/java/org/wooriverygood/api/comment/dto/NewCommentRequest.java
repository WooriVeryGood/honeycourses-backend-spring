package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentRequest {

    private final String email;
    private final String content;

    @Builder
    public NewCommentRequest(String email, String content) {
        this.email = email;
        this.content = content;
    }
}
