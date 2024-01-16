package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentResponse {

    private Long comment_id;
    private String content;
    private String author;

    @Builder
    public NewCommentResponse(Long comment_id, String content, String author) {
        this.comment_id = comment_id;
        this.content = content;
        this.author = author;
    }
}
