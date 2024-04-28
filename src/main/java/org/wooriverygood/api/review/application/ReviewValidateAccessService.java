package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.review.exception.ReviewAccessDeniedException;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewValidateAccessService {

    private final ReviewRepository reviewRepository;

    private final MemberRepository memberRepository;


    public void validateReviewAccess(AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Review review = reviewRepository.findTopByMemberOrderByCreatedAtDesc(member)
                .orElseThrow(ReviewAccessDeniedException::new);

        LocalDateTime now = LocalDateTime.now();
        long distance = ChronoUnit.MONTHS.between(review.getCreatedAt(), now);
        if (distance > 6)
            throw new ReviewAccessDeniedException();
    }

}
