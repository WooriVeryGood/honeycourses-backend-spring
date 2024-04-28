package org.wooriverygood.api.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.member.domain.Member;

@Entity
@Getter
@Table(name = "review_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", referencedColumnName = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @Builder
    public ReviewLike(Long id, Review review, Member member) {
        this.id = id;
        this.review = review;
        this.member = member;
    }

    public void delete() { review = null; }
}
