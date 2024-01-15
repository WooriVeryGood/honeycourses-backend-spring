package org.wooriverygood.api.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.wooriverygood.api.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_category")
    private PostCategory category;

    @Column(name = "post_title", length = 45, nullable = false)
    private String title;

    @Column(name = "post_content", length = 2000)
    private String content;

    @Column(name = "post_author", length = 1000)
    private String author;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes;

    @Column(name = "post_time")
    @CreatedDate
    private LocalDateTime createdAt;

}
