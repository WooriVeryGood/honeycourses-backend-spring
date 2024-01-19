package org.wooriverygood.api.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.exception.CourseNotFoundException;
import org.wooriverygood.api.exception.PostNotFoundException;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.dto.NewReviewResponse;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;

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







}
