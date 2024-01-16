package org.wooriverygood.api.post.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "postLikes")
@Getter
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @Column(name = "member_id")
    private String member;

}
