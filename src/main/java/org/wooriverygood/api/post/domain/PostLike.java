package org.wooriverygood.api.post.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postLikes")
@Getter
@NoArgsConstructor
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @Column
    private String username;


    @Builder
    public PostLike(Long id, Post post, String username) {
        this.id = id;
        this.post = post;
        this.username = username;
    }
    public void delete() {
        this.post = null;
    }

}
