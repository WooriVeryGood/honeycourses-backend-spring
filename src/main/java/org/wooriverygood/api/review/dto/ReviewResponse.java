package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.review.domain.Review;

@Getter
public class ReviewResponse {
    private final int review_id;
    private final int course_id;
    private final String review_content;
    private final String review_title;
    private final String instructor_name;
    private final String taken_semyr;
    private final String grade;
    // private final String author_email;

    @Builder
    public ReviewResponse(Review review) {
        this.review_id = review.getId();
        this.course_id = review.getCourse().getId();
        this.review_content = review.getReviewContent();
        this.review_title = review.getReviewTitle();
        this.instructor_name = review.getInstructorName();
        this.taken_semyr = review.getTakenSemyr();
        this.grade = review.getGrade();
        // this.author_email = review.getAuthor_email();
    }
}
