package org.wooriverygood.api.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.advice.exception.CourseNotFoundException;
import org.wooriverygood.api.advice.exception.general.ReviewNotFoundException;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.domain.ReviewLike;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.dto.NewReviewResponse;
import org.wooriverygood.api.review.dto.ReviewLikeResponse;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    public List<ReviewResponse> findAllReviewsByCourseId(Long courseId) {
        List<Review> reviews = reviewRepository.findAllByCourseId(courseId);
        return reviews.stream().map(ReviewResponse::from).toList();
    }

    @Transactional
    public NewReviewResponse addReview(AuthInfo authInfo, Long courseId, NewReviewRequest newReviewRequest) {
        Courses course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Review review = Review.builder()
                .reviewTitle(newReviewRequest.getReview_title())
                .course(course)
                .instructorName(newReviewRequest.getInstructor_name())
                .takenSemyr(newReviewRequest.getTaken_semyr())
                .reviewContent(newReviewRequest.getReview_content())
                .grade(newReviewRequest.getGrade())
                .authorEmail(authInfo.getUsername())
                .build();
        Review saved = reviewRepository.save(review);
        return createResponse(saved);
    }

    @Transactional
    public ReviewLikeResponse likeReview(Long reviewId, AuthInfo authInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByReviewAndUsername(review, authInfo.getUsername());

        if(reviewLike.isEmpty()) {
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


    private NewReviewResponse createResponse(Review review) {
        return NewReviewResponse.builder()
                .review_id(review.getId())
                .review_title(review.getReviewTitle())
                .instructor_name(review.getInstructorName())
                .taken_semyr(review.getTakenSemyr())
                .review_content(review.getReviewContent())
                .grade(review.getGrade())
                .author_email(review.getAuthorEmail())
                .build();
    }

    private ReviewLikeResponse createReviewLikeResponse(Review review, boolean liked) {
        int likeCount = review.getLikeCount() + (liked ? 1 : -1);
        return ReviewLikeResponse.builder()
                .like_count(likeCount)
                .liked(liked)
                .build();
    }







}
