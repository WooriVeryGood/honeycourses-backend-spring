package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostResponse {

    private final Long postId;

    private final String postTitle;

    private final String postContent;

    private final String postCategory;

    private final String postAuthor;

    private final int postComments;

    private final int postLikes;

    private final LocalDateTime postTime;

    private final boolean liked;

    private final boolean updated;

    private final boolean reported;

    private final int viewCount;


    @Builder
    public PostResponse(Long postId, String postTitle, String postContent, String postCategory, String postAuthor, int postComments, int postLikes, LocalDateTime postTime, boolean liked, boolean updated, boolean reported, int viewCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
        this.postAuthor = postAuthor;
        this.postComments = postComments;
        this.postLikes = postLikes;
        this.postTime = postTime;
        this.liked = liked;
        this.updated = updated;
        this.reported = reported;
        this.viewCount = viewCount;
    }

    public static PostResponse of(Post post, boolean liked) {
        boolean reported = post.getReportCount() >= 5;
        return PostResponse.builder()
                .postId(post.getId())
                .postTitle(reported ? null : post.getTitle().getValue())
                .postContent(reported ? null : post.getContent().getValue())
                .postCategory(post.getCategory().getValue())
                .postAuthor(post.getAuthor())
                .postComments(post.getCommentCount())
                .postLikes(post.getLikeCount())
                .postTime(post.getCreatedAt())
                .liked(liked)
                .updated(post.isUpdated())
                .reported(reported)
                .viewCount(post.getViewCount())
                .build();
    }

}
