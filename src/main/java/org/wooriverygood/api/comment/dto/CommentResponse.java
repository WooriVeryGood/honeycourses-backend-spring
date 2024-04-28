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

    private final Long postId;

    private final int commentLikeCount;

    private final LocalDateTime commentTime;

    private final boolean liked;

    private final List<ReplyResponse> replies;

    private final boolean updated;

    private final boolean reported;

    private final boolean isMine;

    private final long memberId;


    @Builder
    public CommentResponse(
            Long commentId, String commentContent, Long postId,
           int commentLikeCount, LocalDateTime commentTime, boolean liked,
           List<ReplyResponse> replies, boolean updated, boolean reported,
           boolean isMine, long memberId
    ) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.postId = postId;
        this.commentLikeCount = commentLikeCount;
        this.commentTime = commentTime;
        this.liked = liked;
        this.replies = replies;
        this.updated = updated;
        this.reported = reported;
        this.isMine = isMine;
        this.memberId = memberId;
    }


    public static CommentResponse of(Comment comment, List<ReplyResponse> replies, boolean liked, boolean isMine) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentContent(comment.getContent())
                .memberId(comment.getMember().getId())
                .postId(comment.getPost().getId())
                .commentLikeCount(comment.getLikeCount())
                .commentTime(comment.getCreatedAt())
                .liked(liked)
                .replies(replies)
                .updated(comment.isUpdated())
                .reported(comment.isReportedTooMuch())
                .isMine(isMine)
                .build();
    }

    public static CommentResponse softRemovedOf(Comment comment, List<ReplyResponse> replies, boolean isMine) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .commentContent(null)
                .memberId(comment.getMember().getId())
                .postId(comment.getPost().getId())
                .commentLikeCount(comment.getLikeCount())
                .commentTime(comment.getCreatedAt())
                .replies(replies)
                .updated(comment.isUpdated())
                .reported(comment.isReportedTooMuch())
                .isMine(isMine)
                .build();
    }

}
