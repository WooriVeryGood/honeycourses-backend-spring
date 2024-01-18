package org.wooriverygood.api.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.service.ReviewService;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/courses/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> findAllReviewsByCourseId(@PathVariable("id") int courseId) {
        List<ReviewResponse> reviews = reviewService.findAllReviewsByCourseId(courseId);
        return ResponseEntity.ok(reviews);
    }
}
