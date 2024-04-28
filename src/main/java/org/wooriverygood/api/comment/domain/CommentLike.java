package org.wooriverygood.api.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.member.domain.Member;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @Builder
    public CommentLike(Long id, Comment comment, Member member) {
        this.id = id;
        this.comment = comment;
        this.member = member;
    }

    public void delete() {
        comment = null;
    }

}
