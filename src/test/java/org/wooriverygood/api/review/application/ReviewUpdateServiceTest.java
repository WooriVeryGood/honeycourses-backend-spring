package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewUpdateRequest;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewUpdateServiceTest extends MockTest {

    @InjectMocks
    private ReviewUpdateService reviewUpdateService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    private Review review = Review.builder()
            .id(1L)
            .member(member)
            .build();

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

        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(review));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        reviewUpdateService.updateReview(1L, request, authInfo);

        assertAll(
                () -> assertThat(review.getReviewTitle()).isEqualTo(request.getReviewTitle()),
                () -> assertThat(review.getInstructorName()).isEqualTo(request.getInstructorName()),
                () -> assertThat(review.getTakenSemyr()).isEqualTo(request.getTakenSemyr()),
                () -> assertThat(review.getReviewContent()).isEqualTo(request.getReviewContent()),
                () -> assertThat(review.getGrade()).isEqualTo(request.getGrade())
        );
    }

    @Test
    @DisplayName("권한이 없는 리뷰는 수정이 불가능하다.")
    void updateReview_noAuth() {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .build();

        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(review));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Member(5L, "noAuth")));

        assertThatThrownBy(() -> reviewUpdateService.updateReview(review.getId(), request, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}