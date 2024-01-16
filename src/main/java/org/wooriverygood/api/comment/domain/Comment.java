package org.wooriverygood.api.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.post.domain.Post;

@Entity
@Table(name = "testComments")
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;


    @Column(name = "comment_content", length = 200, nullable = false)
    private String content;

    @Column(name = "comment_author", length = 1000)
    private String author;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;

    @Builder
    public Comment(Long id, String content, String author, Post post) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.post = post;
    }

}
