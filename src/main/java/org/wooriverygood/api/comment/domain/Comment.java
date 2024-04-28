package org.wooriverygood.api.comment.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.report.domain.CommentReport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> replies = new ArrayList<>();

    @Column(name = "comment_content", length = 200, nullable = false)
    private String content;

    @Column(name = "comment_author", length = 1000)
    private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    @OneToMany(mappedBy = "comment")
    private List<CommentReport> reports = new ArrayList<>();

    @Column(name = "report_count")
    @ColumnDefault("0")
    private int reportCount;

    @Column(name = "comment_time")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "soft_removed")
    @ColumnDefault("false")
    private boolean softRemoved;

    @ColumnDefault("false")
    private boolean updated;

    @Builder
    public Comment(Long id, String content, String author, Post post,
                   Member member, Comment parent, List<CommentLike> commentLikes,
                   List<CommentReport> reports, boolean softRemoved, boolean updated) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.post = post;
        this.parent = parent;
        this.commentLikes = commentLikes;
        this.reports = reports;
        this.softRemoved = softRemoved;
        this.updated = updated;
        this.member = member;
    }

    public void addCommentLike(CommentLike commentLike) {
        commentLikes.add(commentLike);
    }

    public void deleteCommentLike(CommentLike commentLike) {
        commentLikes.remove(commentLike);
        commentLike.delete();
    }

    public void addReport(CommentReport report) {
        reports.add(report);
    }

    public boolean hasReportByMember(Member member) {
        for (CommentReport report: reports)
            if (report.isOwner(member))
                return true;
        return false;
    }

    public void validateAuthor(String author) {
        if (!this.author.equals(author)) throw new AuthorizationException();
    }

    public void updateContent(String content) {
        this.content = content;
        updated = true;
    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }

    public void deleteReply(Comment reply) {
        replies.remove(reply);
        reply.delete();
    }

    public void delete() {
        parent = null;
    }

    public boolean isParent() {
        return Objects.isNull(parent);
    }

    public boolean isReply() {
        return !isParent();
    }

    public boolean hasNoReply() {
        return replies.isEmpty();
    }

    public void willBeDeleted() {
        softRemoved = true;
    }

    public boolean canDelete() {
        return hasNoReply() && softRemoved;
    }

    public boolean isReportedTooMuch() {
        return reportCount >= 5;
    }

    public String getContent() {
        return isReportedTooMuch() ? null : content;
    }

}
