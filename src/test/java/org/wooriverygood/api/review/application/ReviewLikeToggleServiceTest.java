package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.domain.ReviewLike;
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ReviewLikeToggleServiceTest extends MockTest {

    @InjectMocks
    private ReviewLikeToggleService reviewLikeToggleService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    private final Review review = Review.builder()
            .id(1L)
            .reviewContent("test review")
            .reviewTitle("test review title")
            .instructorName("jiaoshou")
            .takenSemyr("22-23")
            .grade("100")
            .member(member)
            .reviewLikes(new ArrayList<>())
            .updated(false)
            .createdAt(LocalDateTime.of(2022, 6, 13, 12, 00))
            .build();


    @Test
    @DisplayName("특정 리뷰의 좋아요를 1 올린다.")
    void likeReview_up() {
        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(review));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        ReviewLikeResponse response = reviewLikeToggleService.toggleReviewLike(review.getId(), authInfo);

        assertAll(
                () -> assertThat(response.getLikeCount()).isEqualTo(review.getLikeCount() + 1),
                () -> assertThat(response.isLiked()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("특정 리뷰의 좋아요를 1 내린다.")
    void likeReview_down() {
        ReviewLike reviewLike = ReviewLike.builder()
                .id(3L)
                .review(review)
                .member(member)
                .build();

        when(reviewRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(review));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(reviewLikeRepository.findByReviewAndMember(any(Review.class), any(Member.class)))
                .thenReturn(Optional.ofNullable(reviewLike));

        ReviewLikeResponse response = reviewLikeToggleService.toggleReviewLike(review.getId(), authInfo);

        assertThat(response.getLikeCount()).isEqualTo(review.getLikeCount() - 1);
        assertThat(response.isLiked()).isEqualTo(false);
    }

}
