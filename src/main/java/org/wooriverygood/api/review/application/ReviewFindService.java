package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.dto.ReviewsResponse;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewFindService {

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final MemberRepository memberRepository;


    public ReviewsResponse findAllReviewsByCourseId(Long courseId, AuthInfo authInfo) {
        List<Review> reviews = reviewRepository.findAllByCourseId(courseId);
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        return new ReviewsResponse(reviews.stream()
                .map(review -> {
                    boolean liked = reviewLikeRepository.existsByReviewAndMember(review, member);
                    boolean isMine = review.sameAuthor(member);
                    return ReviewResponse.of(review, isMine, liked);
                })
                .toList());
    }

    public ReviewsResponse findMyReviews(AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        List<Review> reviews= member.getReviews();
        return new ReviewsResponse(reviews.stream()
                .map(review -> {
                    boolean liked = reviewLikeRepository.existsByReviewAndMember(review, member);
                    return ReviewResponse.of(review, true, liked);
                })
                .toList());
    }

}
