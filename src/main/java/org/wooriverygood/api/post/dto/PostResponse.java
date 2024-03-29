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

    private final String post_author;

    private final int post_comments;

    private final int post_likes;

    private final LocalDateTime post_time;

    private final boolean liked;

    private final boolean updated;

    private final boolean reported;


    @Builder
    public PostResponse(Long post_id, String post_title, String post_content, String post_category, String post_author, int post_comments, int post_likes, LocalDateTime post_time, boolean liked, boolean updated, boolean reported) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.post_category = post_category;
        this.post_author = post_author;
        this.post_comments = post_comments;
        this.post_likes = post_likes;
        this.post_time = post_time;
        this.liked = liked;
        this.updated = updated;
        this.reported = reported;
    }

    public static PostResponse from(Post post, boolean liked) {
        boolean reported = post.getReportCount() >= 5;
        return PostResponse.builder()
                .post_id(post.getId())
                .post_title(reported ? null : post.getTitle())
                .post_content(reported ? null : post.getContent())
                .post_category(post.getCategory().getValue())
                .post_author(post.getAuthor())
                .post_comments(post.getCommentCount())
                .post_likes(post.getLikeCount())
                .post_time(post.getCreatedAt())
                .liked(liked)
                .updated(post.isUpdated())
                .reported(reported)
                .build();
    }

}
