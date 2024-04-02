package org.wooriverygood.api.comment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {

    private final Long commentId;

    private final String commentContent;

    private final String commentAuthor;

    private final Long postId;

    private final int commentLikeCount;

    private final LocalDateTime commentTime;

    private final boolean liked;

    private final List<ReplyResponse> replies;

    private final boolean updated;

    private final boolean reported;


    @Builder
    public CommentResponse(Long commentId, String commentContent, String commentAuthor, Long postId, int commentLikeCount, LocalDateTime commentTime, boolean liked, List<ReplyResponse> replies, boolean updated, boolean reported) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.commentAuthor = commentAuthor;
        this.postId = postId;
        this.commentLikeCount = commentLikeCount;
        this.commentTime = commentTime;
        this.liked = liked;
        this.replies = replies;
        this.updated = updated;
        this.reported = reported;
    }


    public static CommentResponse of(Comment comment, List<ReplyResponse> replies, boolean liked) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .commentAuthor(comment.getAuthor())
                .postId(comment.getPost().getId())
                .commentLikeCount(comment.getLikeCount())
                .commentTime(comment.getCreatedAt())
                .liked(liked)
                .replies(replies)
                .updated(comment.isUpdated())
                .reported(comment.isReportedTooMuch())
                .build();
    }

    public static CommentResponse softRemovedOf(Comment comment, List<ReplyResponse> replies) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentContent(null)
                .commentAuthor(comment.getAuthor())
                .postId(comment.getPost().getId())
                .commentLikeCount(comment.getLikeCount())
                .commentTime(comment.getCreatedAt())
                .replies(replies)
                .updated(comment.isUpdated())
                .reported(comment.isReportedTooMuch())
                .build();
    }

}
