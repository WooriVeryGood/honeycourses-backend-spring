package org.wooriverygood.api.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.comment.domain.Comment;

@Entity
@Getter
@NoArgsConstructor
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    private Comment comment;

    @Column
    private String username;

    private String message;


    @Builder
    public CommentReport(Long id, Comment comment, String username, String message) {
        this.id = id;
        this.comment = comment;
        this.username = username;
        this.message = message;
    }

    public boolean isOwner(String username) {
        return this.username.equals(username);
    }
}
