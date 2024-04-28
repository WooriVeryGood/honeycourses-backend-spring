package org.wooriverygood.api.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewReviewRequest {

    @NotBlank(message = "제목이 없습니다.")
    private final String reviewTitle;

    private final String instructorName;

    private final String takenSemyr;

    private final String reviewContent;

    private final String grade;


    @Builder
    public NewReviewRequest(String reviewTitle, String instructorName, String takenSemyr,
                            String reviewContent, String grade) {
        this.reviewTitle = reviewTitle;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.reviewContent = reviewContent;
        this.grade = grade;
    }

}
