package org.wooriverygood.api.review.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.review.application.*;
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.auth.Login;

@RestController
@RequiredArgsConstructor
public class ReviewApi {

    private final ReviewLikeToggleService reviewLikeToggleService;

    private final ReviewCreateService reviewCreateService;

    private final ReviewDeleteService reviewDeleteService;

    private final ReviewFindService reviewFindService;

    private final ReviewUpdateService reviewUpdateService;

    private final ReviewValidateAccessService reviewValidateAccessService;


    @GetMapping("/courses/{id}/reviews")
    public ResponseEntity<ReviewsResponse> findAllReviewsByCourseId(@PathVariable("id") Long courseId,
                                                                    @Login AuthInfo authInfo) {
        reviewValidateAccessService.validateReviewAccess(authInfo);
        ReviewsResponse response = reviewFindService.findAllReviewsByCourseId(courseId, authInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/courses/{id}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable("id") Long courseId, @Login AuthInfo authInfo,
                                          @Valid @RequestBody NewReviewRequest request) {
        reviewCreateService.addReview(authInfo, courseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/reviews/{rid}/like")
    public ResponseEntity<ReviewLikeResponse> likeReview(@PathVariable("rid") Long reviewId,
                                                         @Login AuthInfo authInfo) {
        ReviewLikeResponse response= reviewLikeToggleService.toggleReviewLike(reviewId, authInfo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reviews/me")
    public ResponseEntity<ReviewsResponse> findMyReviews(@Login AuthInfo authInfo) {
        ReviewsResponse response = reviewFindService.findMyReviews(authInfo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable("id") Long reviewId,
                                             @Valid @RequestBody ReviewUpdateRequest request,
                                             @Login AuthInfo authInfo) {
        reviewUpdateService.updateReview(reviewId, request, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable("id") Long reviewId,
                                             @Login AuthInfo authInfo) {
        reviewDeleteService.deleteReview(reviewId, authInfo);
        return ResponseEntity.noContent().build();
    }

}
