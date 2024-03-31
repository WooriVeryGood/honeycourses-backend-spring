package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.review.exception.ReviewAccessDeniedException;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReviewValidateAccessServiceTest extends MockTest {

    @InjectMocks
    private ReviewValidateAccessService reviewValidateAccessService;

    @Mock
    private ReviewRepository reviewRepository;

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();


    @Test
    @DisplayName("마지막으로 작성한 리뷰가 현재 기준으로 6개월보다 가깝다면, 예외를 발생시키지 않는다.")
    void canAccessReviews_true() {
        Review review = Review.builder()
                .createdAt(LocalDateTime.now().minusMonths(6))
                .build();

        when(reviewRepository.findTopByAuthorEmailOrderByCreatedAtDesc(any(String.class)))
                .thenReturn(Optional.ofNullable(review));

        reviewValidateAccessService.validateReviewAccess(authInfo);
    }

    @Test
    @DisplayName("리뷰를 작성하지 않았다면, 예외를 발생시킨다.")
    void canAccessReviews_false_noReview() {
        when(reviewRepository.findTopByAuthorEmailOrderByCreatedAtDesc(any(String.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewValidateAccessService.validateReviewAccess(authInfo))
                .isInstanceOf(ReviewAccessDeniedException.class);
    }

    @Test
    @DisplayName("마지막으로 작성한 리뷰가 현재 기준으로 6개월보다 멀다면, 예외를 발생시킨다.")
    void canAccessReviews_false_sixMonths() {
        Review review = Review.builder()
                .createdAt(LocalDateTime.of(2022, 6, 13, 12, 00))
                .build();

        when(reviewRepository.findTopByAuthorEmailOrderByCreatedAtDesc(any(String.class)))
                .thenReturn(Optional.ofNullable(review));

        assertThatThrownBy(() -> reviewValidateAccessService.validateReviewAccess(authInfo))
                .isInstanceOf(ReviewAccessDeniedException.class);
    }

}