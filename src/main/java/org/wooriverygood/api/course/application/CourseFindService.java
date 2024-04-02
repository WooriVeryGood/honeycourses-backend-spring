package org.wooriverygood.api.course.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.exception.CourseNotFoundException;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.dto.CourseNameResponse;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.dto.CoursesResponse;
import org.wooriverygood.api.course.repository.CourseRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseFindService {

    private final CourseRepository courseRepository;

    public CoursesResponse findAll() {
        List<CourseResponse> responses = courseRepository.findAll()
                .stream()
                .map(CourseResponse::of)
                .toList();

        return new CoursesResponse(responses);
    }

    public CourseNameResponse findCourseName(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        return new CourseNameResponse(course.getName());
    }

}
