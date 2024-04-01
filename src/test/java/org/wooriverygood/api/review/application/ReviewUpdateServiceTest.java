package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewUpdateRequest;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewUpdateServiceTest extends MockTest {

    @InjectMocks
    private ReviewUpdateService reviewUpdateService;

    @Mock
    private ReviewRepository reviewRepository;

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    @Mock
    private Review review;

    @Test
    @DisplayName("권한이 있는 리뷰를 수정한다.")
    void updateReview() {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .reviewTitle("new title")
                .reviewContent("new content")
                .instructorName("jiaoshou")
                .takenSemyr("18-19")
                .grade("100")
                .build();

        when(reviewRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(review));

        reviewUpdateService.updateReview(1L, request, authInfo);

        verify(review).validateAuthor(authInfo.getUsername());
        verify(review).updateTitle(request.getReviewTitle());
        verify(review).updateInstructorName(request.getInstructorName());
        verify(review).updateTakenSemyr(request.getTakenSemyr());
        verify(review).updateContent(request.getReviewContent());
        verify(review).updateGrade(request.getGrade());
        verify(review).updateAuthor(authInfo.getUsername());
    }

    @Test
    @DisplayName("권한이 없는 리뷰는 수정이 불가능하다.")
    void updateReview_noAuth() {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .build();

        when(reviewRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(review));
        doThrow(new AuthorizationException())
                .when(review)
                .validateAuthor(anyString());

        assertThatThrownBy(() -> reviewUpdateService.updateReview(1L, request, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}