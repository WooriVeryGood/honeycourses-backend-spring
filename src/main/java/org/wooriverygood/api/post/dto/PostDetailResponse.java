package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.post.domain.Post;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostDetailResponse {

    private final Long postId;

    private final String postTitle;

    private final String postContent;

    private final String postCategory;

    private final long memberId;

    private final boolean isMine;

    private final int postComments;

    private final int postLikes;

    private final LocalDateTime postTime;

    private final boolean liked;

    private final boolean updated;

    private final boolean reported;

    private final int viewCount;


    @Builder
    public PostDetailResponse(Long postId, String postTitle, String postContent,
                              String postCategory, long memberId, int postComments,
                              int postLikes, LocalDateTime postTime, boolean liked,
                              boolean isMine, boolean updated, boolean reported, int viewCount) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postCategory = postCategory;
        this.memberId = memberId;
        this.isMine = isMine;
        this.postComments = postComments;
        this.postLikes = postLikes;
        this.postTime = postTime;
        this.liked = liked;
        this.updated = updated;
        this.reported = reported;
        this.viewCount = viewCount;
    }

    public static PostDetailResponse of(Post post, boolean liked, boolean isMine) {
        return PostDetailResponse.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .postContent(post.getContent())
                .postCategory(post.getCategory().getValue())
                .isMine(isMine)
                .memberId(post.getMember().getId())
                .postComments(post.getCommentCount())
                .postLikes(post.getLikeCount())
                .postTime(post.getCreatedAt())
                .liked(liked)
                .updated(post.isUpdated())
                .reported(post.isReportedTooMuch())
                .viewCount(post.getViewCount())
                .build();
    }

}
