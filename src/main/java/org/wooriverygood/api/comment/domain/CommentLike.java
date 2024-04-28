package org.wooriverygood.api.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "commentLikes")
@Getter
@NoArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "comment_id")
    private Comment comment;

    private String username;


    @Builder
    public CommentLike(Long id, Comment comment, String username) {
        this.id = id;
        this.comment = comment;
        this.username = username;
    }

    public void delete() {
        comment = null;
    }

}
