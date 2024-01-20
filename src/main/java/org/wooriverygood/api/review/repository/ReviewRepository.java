package org.wooriverygood.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCourseId(Long courseId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE reviews SET like_count = like_count + 1 WHERE review_id = :reviewId", nativeQuery = true)
    void increaseLikeCount(Long reviewId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE reviews SET like_count = like_count - 1 WHERE review_id = :reviewId", nativeQuery = true)
    void decreaseLikeCount(Long reviewId);
}
