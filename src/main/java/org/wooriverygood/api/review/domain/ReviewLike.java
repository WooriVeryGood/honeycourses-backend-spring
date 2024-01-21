package org.wooriverygood.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviewLikes")
@Getter
@NoArgsConstructor
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "review_id", referencedColumnName = "review_id")
    private Review review;

    @Column
    private String username;
    @Builder
    public ReviewLike(Long id, Review review, String username) {
        this.id = id;
        this.review = review;
        this.username = username;
    }

    public void delete() { review = null; }
}
