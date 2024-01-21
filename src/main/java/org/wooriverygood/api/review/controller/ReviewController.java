package org.wooriverygood.api.review.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.dto.NewReviewResponse;
import org.wooriverygood.api.review.dto.ReviewLikeResponse;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.service.ReviewService;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.support.Login;

import java.util.List;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/courses/{id}/reviews")
    public ResponseEntity<List<ReviewResponse>> findAllReviewsByCourseId(@PathVariable("id") Long courseId, @Login AuthInfo authInfo) {
        List<ReviewResponse> reviews = reviewService.findAllReviewsByCourseId(courseId, authInfo);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/courses/{id}/reviews")
    public ResponseEntity<NewReviewResponse> addReview(@PathVariable("id") Long courseId, @Login AuthInfo authInfo, @Valid @RequestBody NewReviewRequest newReviewRequest) {
        NewReviewResponse response = reviewService.addReview(authInfo, courseId, newReviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/courses/reviews/{rid}")
    public ResponseEntity<ReviewLikeResponse> likeReview(@PathVariable("rid") Long reviewId, @Login AuthInfo authInfo) {
        ReviewLikeResponse response= reviewService.likeReview(reviewId, authInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/reviews/me")
    public ResponseEntity<List<ReviewResponse>> findMyPosts(@Login AuthInfo authInfo) {
        List<ReviewResponse> reviewResponses = reviewService.findMyReviews(authInfo);
        return ResponseEntity.ok(reviewResponses);
    }

}
