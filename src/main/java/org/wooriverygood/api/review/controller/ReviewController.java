package org.wooriverygood.api.review.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.dto.NewReviewResponse;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.service.ReviewService;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.support.Login;

import java.util.List;

@RestController
@RequestMapping("/courses/{id}/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> findAllReviewsByCourseId(@PathVariable("id") Long courseId) {
        List<ReviewResponse> reviews = reviewService.findAllReviewsByCourseId(courseId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<NewReviewResponse> addReview(@PathVariable("id") Long courseId, @Login AuthInfo authInfo, @Valid @RequestBody NewReviewRequest newReviewRequest) {
        NewReviewResponse response = reviewService.addReview(authInfo, courseId, newReviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
