package org.wooriverygood.api.course.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CoursesResponse {

    private final List<CourseResponse> courses;


    public CoursesResponse(List<CourseResponse> courses) {
        this.courses = courses;
    }

}
