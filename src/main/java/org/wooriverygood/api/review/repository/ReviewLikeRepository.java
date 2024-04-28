package org.wooriverygood.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.domain.ReviewLike;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewAndMember(Review review, Member member);
    boolean existsByReviewAndMember(Review review, Member member);
    void deleteAllByReview(Review review);
}
