package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.exception.ReviewNotFoundException;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReviewDeleteServiceTest extends MockTest {

    @InjectMocks
    private ReviewDeleteService reviewDeleteService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    private Course course = Course.builder()
            .id(1L)
            .build();

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    @Mock
    private Review review;

    @Test
    @DisplayName("권한이 있는 리뷰를 삭제한다.")
    void deleteReview() {
        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(review));
        when(review.getCourse())
                .thenReturn(course);

        reviewDeleteService.deleteReview(1L, authInfo);

        verify(review).validateAuthor(authInfo.getUsername());
        verify(reviewLikeRepository).deleteAllByReview(review);
        verify(reviewRepository).delete(review);
        verify(courseRepository).decreaseReviewCount(course.getId());
    }

    @Test
    @DisplayName("권한이 없는 리뷰는 삭제가 불가능하다.")
    void deleteReview_exception_noAuth() {
        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(review));
        doThrow(new AuthorizationException())
                .when(review)
                .validateAuthor(anyString());

        assertThatThrownBy(() -> reviewDeleteService.deleteReview(1L, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("리뷰를 찾을 수 없으면, 예외를 발생시킨다.")
    void deleteReview_exception_reviewNotFound() {
        when(reviewRepository.findById(anyLong()))
                .thenThrow(new ReviewNotFoundException());

        assertThatThrownBy(() -> reviewDeleteService.deleteReview(1L, authInfo))
                .isInstanceOf(ReviewNotFoundException.class);
    }

}