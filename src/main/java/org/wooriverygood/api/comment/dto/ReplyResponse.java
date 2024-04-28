package org.wooriverygood.api.comment.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReplyResponse {

    private final Long replyId;

    private final String replyContent;

    private final int replyLikeCount;

    private final LocalDateTime replyTime;

    private final boolean liked;

    private final boolean updated;

    private final boolean reported;

    private final boolean isMine;

    private final long memberId;


    @Builder
    public ReplyResponse(
            Long replyId, String replyContent, int replyLikeCount,
            LocalDateTime replyTime, boolean liked, boolean updated,
            boolean reported, boolean isMine, long memberId
    ) {
        this.replyId = replyId;
        this.replyContent = replyContent;
        this.isMine = isMine;
        this.memberId = memberId;
        this.replyLikeCount = replyLikeCount;
        this.replyTime = replyTime;
        this.liked = liked;
        this.updated = updated;
        this.reported = reported;
    }

    public static ReplyResponse of(Comment reply, boolean liked, boolean isMine) {
        return ReplyResponse.builder()
                .replyId(reply.getId())
                .replyContent(reply.getContent())
                .memberId(reply.getMember().getId())
                .replyLikeCount(reply.getLikeCount())
                .replyTime(reply.getCreatedAt())
                .liked(liked)
                .updated(reply.isUpdated())
                .reported(reply.isReportedTooMuch())
                .isMine(isMine)
                .build();
    }

}
