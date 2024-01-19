package org.wooriverygood.api.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.review.domain.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByCourseId(Long courseId);
}
