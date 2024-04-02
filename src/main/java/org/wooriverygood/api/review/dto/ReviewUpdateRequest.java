package org.wooriverygood.api.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewUpdateRequest {

    @NotBlank(message = "제목이 없습니다.")
    private String reviewTitle;

    private String instructorName;

    private String takenSemyr;

    private String reviewContent;

    private String grade;


    @Builder
    public ReviewUpdateRequest(String reviewTitle, String instructorName, String takenSemyr,
                               String reviewContent, String grade) {
        this.reviewTitle = reviewTitle;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.reviewContent = reviewContent;
        this.grade = grade;
    }

}
