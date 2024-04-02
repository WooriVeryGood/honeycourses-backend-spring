package org.wooriverygood.api.report.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.post.domain.Post;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @Column
    private String username;

    private String message;


    @Builder
    public PostReport(Long id, Post post, String username, String message) {
        this.id = id;
        this.post = post;
        this.username = username;
        this.message = message;
    }

    public boolean isOwner(String username) {
        return this.username.equals(username);
    }

}
