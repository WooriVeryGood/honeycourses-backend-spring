package org.wooriverygood.api.review.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.review.application.ReviewFindService;
import org.wooriverygood.api.review.application.ReviewValidateAccessService;
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.review.application.ReviewService;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.auth.Login;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewService reviewService;

    private final ReviewFindService reviewFindService;

    private final ReviewValidateAccessService reviewValidateAccessService;


    @GetMapping("/courses/{id}/reviews")
    public ResponseEntity<ReviewsResponse> findAllReviewsByCourseId(@PathVariable("id") Long courseId,
                                                                    @Login AuthInfo authInfo) {
        reviewValidateAccessService.validateReviewAccess(authInfo);
        ReviewsResponse response = reviewFindService.findAllReviewsByCourseId(courseId, authInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/courses/{id}/reviews")
    public ResponseEntity<NewReviewResponse> addReview(@PathVariable("id") Long courseId, @Login AuthInfo authInfo, @Valid @RequestBody NewReviewRequest newReviewRequest) {
        NewReviewResponse response = reviewService.addReview(authInfo, courseId, newReviewRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/reviews/{rid}/like")
    public ResponseEntity<ReviewLikeResponse> likeReview(@PathVariable("rid") Long reviewId, @Login AuthInfo authInfo) {
        ReviewLikeResponse response= reviewService.likeReview(reviewId, authInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reviews/me")
    public ResponseEntity<ReviewsResponse> findMyReviews(@Login AuthInfo authInfo) {
        ReviewsResponse response = reviewFindService.findMyReviews(authInfo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reviews/{rid}")
    public ResponseEntity<ReviewUpdateResponse> updateReview(@PathVariable("rid") Long reviewId,
                                                             @Valid @RequestBody ReviewUpdateRequest reviewUpdateRequest,
                                                             @Login AuthInfo authInfo) {
        ReviewUpdateResponse response = reviewService.updateReview(reviewId, reviewUpdateRequest, authInfo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/reviews/{rid}")
    public ResponseEntity<ReviewDeleteResponse> deleteReview(@PathVariable("rid") Long reviewId,
                                                             @Login AuthInfo authInfo) {
        ReviewDeleteResponse response = reviewService.deleteReview(reviewId, authInfo);
        return ResponseEntity.ok(response);
    }

}
