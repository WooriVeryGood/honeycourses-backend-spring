package org.wooriverygood.api.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<ReviewResponse> findAllReviewsByCourseId(int courseId) {
        List<Review> reviews = reviewRepository.findAllByCourseId(courseId);
        return reviews.stream().map(ReviewResponse::from).toList();
    }




}
