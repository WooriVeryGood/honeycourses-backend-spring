package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.exception.ReviewNotFoundException;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.domain.ReviewLike;
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewLikeToggleService {

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final MemberRepository memberRepository;


    public ReviewLikeResponse toggleReviewLike(Long reviewId, AuthInfo authInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByReviewAndMember(review, member);

        if (reviewLike.isEmpty()) {
            addReviewLike(review, authInfo.getUsername());
            return createReviewLikeResponse(review, true);
        }

        deleteReviewLike(review, reviewLike.get());
        return createReviewLikeResponse(review, false);

    }

    private void addReviewLike(Review review, String username) {
        ReviewLike reviewLike = ReviewLike.builder()
                .review(review)
                .username(username)
                .build();

        review.addReviewLike(reviewLike);
        reviewLikeRepository.save(reviewLike);
        reviewRepository.increaseLikeCount(review.getId());
    }

    private void deleteReviewLike(Review review, ReviewLike reviewLike) {
        review.deleteReviewLike(reviewLike);
        reviewRepository.decreaseLikeCount(review.getId());
    }

    private ReviewLikeResponse createReviewLikeResponse(Review review, boolean liked) {
        int likeCount = review.getLikeCount() + (liked ? 1 : -1);
        return ReviewLikeResponse.builder()
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }

}
