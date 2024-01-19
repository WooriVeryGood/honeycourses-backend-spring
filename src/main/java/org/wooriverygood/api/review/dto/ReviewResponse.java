package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.review.domain.Review;

@Getter
public class ReviewResponse {
    private final Long review_id;
    private final Long course_id;
    private final String review_content;
    private final String review_title;
    private final String instructor_name;
    private final String taken_semyr;
    private final String grade;

    @Builder
    public ReviewResponse(Long review_id, Long course_id, String review_content, String review_title, String instructor_name, String taken_semyr, String grade) {
        this.review_id = review_id;
        this.course_id = course_id;
        this.review_content = review_content;
        this.review_title = review_title;
        this.instructor_name = instructor_name;
        this.taken_semyr = taken_semyr;
        this.grade = grade;
    }

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .review_id(review.getId())
                .course_id(review.getCourse().getId())
                .review_content(review.getReviewContent())
                .review_title(review.getReviewTitle())
                .instructor_name(review.getInstructorName())
                .taken_semyr(review.getTakenSemyr())
                .grade(review.getGrade())
                .build();
    }
}
