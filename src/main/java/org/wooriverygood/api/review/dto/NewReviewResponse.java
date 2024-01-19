package org.wooriverygood.api.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewReviewResponse {
    private final Long review_id;
    private final String review_title;
    private final String instructor_name;
    private final String taken_semyr;
    private final String review_content;
    private final String grade;
    private final String author_email;


    @Builder
    public NewReviewResponse(Long review_id, String review_title, String instructor_name, String taken_semyr, String review_content, String grade, String author_email) {
        this.review_id = review_id;
        this.review_title = review_title;
        this.instructor_name = instructor_name;
        this.taken_semyr = taken_semyr;
        this.review_content = review_content;
        this.grade = grade;
        this.author_email = author_email;
    }
}
