package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.exception.ReviewNotFoundException;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewDeleteService {

    private final CourseRepository courseRepository;

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;

    private final MemberRepository memberRepository;


    @Transactional
    public void deleteReview(Long reviewId, AuthInfo authInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        review.validateAuthor(member);

        reviewLikeRepository.deleteAllByReview(review);
        reviewRepository.delete(review);
        courseRepository.decreaseReviewCount(review.getCourse().getId());
    }

}
