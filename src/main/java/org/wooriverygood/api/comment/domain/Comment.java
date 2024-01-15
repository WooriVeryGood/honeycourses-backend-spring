package org.wooriverygood.api.comment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.wooriverygood.api.post.domain.Post;

@Entity
@Table(name = "testComments")
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;


    @Column(name = "comment_content", length = 200, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "post_id")
    private Post post;
}
