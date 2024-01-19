package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewReviewRequest {
    private final String review_title;
    private final String instructor_name;
    private final String taken_semyr;
    private final String review_content;
    private final String grade;

    @Builder
    public NewReviewRequest(String review_title, String instructor_name, String taken_semyr, String review_content, String grade) {
        this.review_title = review_title;
        this.instructor_name = instructor_name;
        this.taken_semyr = taken_semyr;
        this.review_content = review_content;
        this.grade = grade;
    }
}
