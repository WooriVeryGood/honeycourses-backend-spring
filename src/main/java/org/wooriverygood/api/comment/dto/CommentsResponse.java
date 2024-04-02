package org.wooriverygood.api.comment.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommentsResponse {

    private final List<CommentResponse> comments;


    public CommentsResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }

}
