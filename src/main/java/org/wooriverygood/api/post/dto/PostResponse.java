package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.post.domain.Post;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long post_id;
    private final String post_title;
    private final String post_content;
    private final String post_category;
    private final int post_comments;
    private final int post_likes;
    private final LocalDateTime post_time;
    private final boolean isMine;

    @Builder
    public PostResponse(Long post_id, String post_title, String post_content, String post_category, int post_comments, int post_likes, LocalDateTime post_time, boolean isMine) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_category = post_category;
        this.post_comments = post_comments;
        this.post_likes = post_likes;
        this.post_time = post_time;
        this.isMine = isMine;
    }

    public static PostResponse from(Post post, boolean isMine) {
        return PostResponse.builder()
                .post_id(post.getId())
                .post_title(post.getTitle())
                .post_content(post.getContent())
                .post_category(post.getCategory().getValue())
                .post_comments(post.getComments().size())
                .post_likes(post.getPostLikes().size())
                .post_time(post.getCreatedAt())
                .isMine(isMine)
                .build();
    }

}
