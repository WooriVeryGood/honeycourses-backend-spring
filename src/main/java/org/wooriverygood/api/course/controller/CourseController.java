package org.wooriverygood.api.course.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> findAllCourses() {
        List<CourseResponse>courses = courseService.findAll()
                .stream()
                .map(CourseResponse::new)
                .toList();
        return ResponseEntity.ok().body(courses);
    }


}
