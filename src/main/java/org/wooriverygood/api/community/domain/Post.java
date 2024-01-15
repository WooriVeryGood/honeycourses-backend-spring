package org.wooriverygood.api.community.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
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

    @Column(name = "post_time")
    @CreatedDate
    private LocalDateTime createdAt;

}
