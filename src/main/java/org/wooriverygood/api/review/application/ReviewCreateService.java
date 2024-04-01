package org.wooriverygood.api.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.advice.exception.CourseNotFoundException;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewCreateService {

    private final CourseRepository courseRepository;

    private final ReviewRepository reviewRepository;


    @Transactional
    public void addReview(AuthInfo authInfo, Long courseId, NewReviewRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Review review = createReview(course, authInfo, request);

        reviewRepository.save(review);
        courseRepository.increaseReviewCount(review.getCourse().getId());
    }

    private Review createReview(Course course, AuthInfo authInfo, NewReviewRequest request) {
        return Review.builder()
                .reviewTitle(request.getReviewTitle())
                .course(course)
                .instructorName(request.getInstructorName())
                .takenSemyr(request.getTakenSemyr())
                .reviewContent(request.getReviewContent())
                .grade(request.getGrade())
                .authorEmail(authInfo.getUsername())
                .build();
    }

}
