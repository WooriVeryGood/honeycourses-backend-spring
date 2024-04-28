package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewUpdateRequest;
import org.wooriverygood.api.review.exception.ReviewNotFoundException;
import org.wooriverygood.api.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewUpdateService {

    private final ReviewRepository reviewRepository;

    private final MemberRepository memberRepository;


    @Transactional
    public void updateReview(Long reviewId, ReviewUpdateRequest request, AuthInfo authInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        review.validateAuthor(member);

        review.updateTitle(request.getReviewTitle());
        review.updateInstructorName(request.getInstructorName());
        review.updateTakenSemyr(request.getTakenSemyr());
        review.updateContent(request.getReviewContent());
        review.updateGrade(request.getGrade());
    }

}
