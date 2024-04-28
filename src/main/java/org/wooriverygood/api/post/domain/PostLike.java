package org.wooriverygood.api.post.domain;

import jakarta.persistence.*;
import lombok.*;
import org.wooriverygood.api.member.domain.Member;

@Entity
@Getter
@Table(name = "post_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @Builder
    public PostLike(Long id, Post post, Member member) {
        this.id = id;
        this.post = post;
        this.member = member;
    }

    public void delete() {
        this.post = null;
    }

}
