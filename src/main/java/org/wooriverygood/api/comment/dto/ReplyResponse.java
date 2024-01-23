package org.wooriverygood.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
public class ReplyResponse {

    private final Long reply_id;
    private final String reply_content;
    private final String reply_author;
    private final int reply_likes;
    private final LocalDateTime reply_time;
    private final boolean liked;


    @Builder

    public ReplyResponse(Long reply_id, String reply_content, String reply_author, int reply_likes, LocalDateTime reply_time, boolean liked) {
        this.reply_id = reply_id;
        this.reply_content = reply_content;
        this.reply_author = reply_author;
        this.reply_likes = reply_likes;
        this.reply_time = reply_time;
        this.liked = liked;
    }

    public static ReplyResponse from(Comment reply, boolean liked) {
        return ReplyResponse.builder()
                .reply_id(reply.getId())
                .reply_content(reply.getContent())
                .reply_author(reply.getAuthor())
                .reply_likes(reply.getLikeCount())
                .reply_time(reply.getCreatedAt())
                .liked(liked)
                .build();
    }

}
