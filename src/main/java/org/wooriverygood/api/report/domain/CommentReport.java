package org.wooriverygood.api.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.member.domain.Member;

@Entity
@Getter
@Table(name = "comment_reports")
@NoArgsConstructor
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String message;


    @Builder
    public CommentReport(Long id, Comment comment, Member member, String message) {
        this.id = id;
        this.comment = comment;
        this.member = member;
        this.message = message;
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }
}
