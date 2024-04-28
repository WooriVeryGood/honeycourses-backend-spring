package org.wooriverygood.api.course.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseNameResponse {

    private final String courseName;

    public CourseNameResponse(String courseName) {
        this.courseName = courseName;
    }

}
