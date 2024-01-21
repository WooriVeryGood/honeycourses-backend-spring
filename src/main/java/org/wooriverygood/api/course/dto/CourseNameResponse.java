package org.wooriverygood.api.course.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CourseNameResponse {
    private final String course_name;

    @Builder
    public CourseNameResponse(String course_name) {
        this.course_name = course_name;
    }
}
