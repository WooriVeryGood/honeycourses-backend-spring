package org.wooriverygood.api.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.course.dto.CourseNameResponse;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.course.dto.NewCourseResponse;
import org.wooriverygood.api.course.service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> findAllCourses() {
        List<CourseResponse>responses = courseService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<NewCourseResponse> addCourse(@Valid @RequestBody NewCourseRequest newCourseRequest) {
        NewCourseResponse response = courseService.addCourse(newCourseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<CourseNameResponse> getCourseName(@PathVariable("id") Long courseId) {
        CourseNameResponse response = courseService.getCourseName(courseId);
        return ResponseEntity.ok(response);
    }
}
