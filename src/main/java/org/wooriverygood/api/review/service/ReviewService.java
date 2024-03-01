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
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    public List<ReviewResponse> findAllReviewsByCourseId(Long courseId, AuthInfo authInfo) {
        Courses course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        List<Review> reviews = reviewRepository.findAllByCourseId(courseId);

        if(authInfo.getUsername() == null) {
            return reviews.stream()
                    .map(review -> ReviewResponse.from(review, false, reviewLikeRepository.existsByReviewAndUsername(review, authInfo.getUsername())))
                    .toList();
        }
        return reviews.stream()
                .map(review -> ReviewResponse.from(review, review.isSameAuthor(authInfo.getUsername()), reviewLikeRepository.existsByReviewAndUsername(review, authInfo.getUsername())))
                .toList();
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
        reviewRepository.increaseReviewCount(review.getCourse().getId());
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

    public List<ReviewResponse> findMyReviews(AuthInfo authInfo) {
        List<Review> reviews= reviewRepository.findByAuthorEmail(authInfo.getUsername());
        return reviews.stream().map(review -> ReviewResponse.from(review, true, reviewLikeRepository.existsByReviewAndUsername(review, authInfo.getUsername()))).toList();
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


    @Transactional
    public ReviewUpdateResponse updateReview(Long reviewId, ReviewUpdateRequest reviewUpdateRequest, AuthInfo authInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
//        review.validateAuthor(authInfo.getUsername());

        review.updateReview(reviewUpdateRequest.getReview_title(), reviewUpdateRequest.getInstructor_name(), reviewUpdateRequest.getTaken_semyr(), reviewUpdateRequest.getReview_content(), reviewUpdateRequest.getGrade(), authInfo.getUsername());

        return ReviewUpdateResponse.builder()
                .review_id(review.getId())
                .build();
    }

    @Transactional
    public ReviewDeleteResponse deleteReview(Long reviewId, AuthInfo authInfo) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
        review.validateAuthor(authInfo.getUsername());

        reviewLikeRepository.deleteAllByReview(review);
        reviewRepository.delete(review);
        reviewRepository.decreaseReviewCount(review.getCourse().getId());

        return ReviewDeleteResponse.builder()
                .review_id(reviewId)
                .build();
    }

    public boolean canAccessReviews(AuthInfo authInfo) {
        Optional<Review> review = reviewRepository.findTopByAuthorEmailOrderByCreatedAtDesc(authInfo.getUsername());
        if (review.isEmpty()) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        long distance = ChronoUnit.MONTHS.between(review.get().getCreatedAt(), now);
        return distance <= 6;
    }
}
