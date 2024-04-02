package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.dto.ReviewsResponse;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewFindService {

    private final ReviewRepository reviewRepository;

    private final ReviewLikeRepository reviewLikeRepository;


    public ReviewsResponse findAllReviewsByCourseId(Long courseId, AuthInfo authInfo) {
        List<Review> reviews = reviewRepository.findAllByCourseId(courseId);

        return new ReviewsResponse(reviews.stream()
                .map(review -> {
                    boolean liked = reviewLikeRepository.existsByReviewAndUsername(review, authInfo.getUsername());
                    return ReviewResponse.of(review,
                            review.isSameAuthor(authInfo.getUsername()),
                            liked);
                })
                .toList());
    }

    public ReviewsResponse findMyReviews(AuthInfo authInfo) {
        List<Review> reviews= reviewRepository.findByAuthorEmail(authInfo.getUsername());
        return new ReviewsResponse(reviews.stream()
                .map(review -> {
                    boolean liked = reviewLikeRepository.existsByReviewAndUsername(review, authInfo.getUsername());
                    return ReviewResponse.of(review, true, liked);
                })
                .toList());
    }

}
