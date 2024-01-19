package org.wooriverygood.api.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wooriverygood.api.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "testComments")
@Getter
@EntityListeners(AuditingEntityListener.class)
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

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    @Column(name = "comment_time")
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Comment(Long id, String content, String author, Post post, List<CommentLike> commentLikes) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.post = post;
        this.commentLikes = commentLikes;
    }

    public void addCommentLike(CommentLike commentLike) {
        commentLikes.add(commentLike);
    }

    public void deleteCommentLike(CommentLike commentLike) {
        commentLikes.remove(commentLike);
        commentLike.delete();
    }

}
