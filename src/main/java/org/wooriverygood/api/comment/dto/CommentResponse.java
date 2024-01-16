package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.comment.domain.Comment;

@Getter
public class CommentResponse {

    private final Long comment_id;
    private final String comment_content;
    private final String comment_author;
    private final Long post_id;


    @Builder
    public CommentResponse(Long comment_id, String comment_content, String comment_author, Long post_id) {
        this.comment_id = comment_id;
        this.comment_content = comment_content;
        this.comment_author = comment_author;
        this.post_id = post_id;
    }


    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .comment_id(comment.getId())
                .comment_content(comment.getContent())
                .comment_author(comment.getAuthor())
                .post_id(comment.getPost().getId())
                .build();
    }
}
