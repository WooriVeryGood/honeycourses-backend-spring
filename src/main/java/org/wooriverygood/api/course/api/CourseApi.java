package org.wooriverygood.api.course.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.course.application.CourseFindService;
import org.wooriverygood.api.course.dto.*;
import org.wooriverygood.api.course.application.CourseCreateService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseApi {

    private final CourseCreateService courseCreateService;

    private final CourseFindService courseFindService;


    @GetMapping
    public ResponseEntity<CoursesResponse> findAllCourses() {
        CoursesResponse response = courseFindService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> addCourse(@Valid @RequestBody NewCourseRequest newCourseRequest) {
        courseCreateService.addCourse(newCourseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<CourseNameResponse> findCourseName(@PathVariable("id") Long courseId) {
        CourseNameResponse response = courseFindService.findCourseName(courseId);
        return ResponseEntity.ok(response);
    }

}
