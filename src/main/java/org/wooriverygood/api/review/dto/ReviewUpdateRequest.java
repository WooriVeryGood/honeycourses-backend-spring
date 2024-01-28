package org.wooriverygood.api.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {
    @NotBlank(message = "제목이 없습니다.")
    private String review_title;
    private String instructor_name;
    private String taken_semyr;
    private String review_content;
    private String grade;

    @Builder
    public ReviewUpdateRequest(String review_title, String instructor_name, String taken_semyr, String review_content, String grade) {
        this.review_title = review_title;
        this.instructor_name = instructor_name;
        this.taken_semyr = taken_semyr;
        this.review_content = review_content;
        this.grade = grade;
    }
}
