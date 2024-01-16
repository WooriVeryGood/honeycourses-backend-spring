package org.wooriverygood.api.review.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.repository.ReviewRepository;

import java.util.List;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> findAllByCourseId(int courseId) {
        return reviewRepository.findAllByCourseId(courseId);
    }




}
