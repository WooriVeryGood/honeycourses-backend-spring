package org.wooriverygood.api.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.advice.exception.CourseNotFoundException;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.dto.CourseNameResponse;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.course.dto.NewCourseResponse;
import org.wooriverygood.api.course.repository.CourseRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;

    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream().map(CourseResponse::from).toList();
    }

    @Transactional
    public NewCourseResponse addCourse(NewCourseRequest newCourseRequest) {
        Course course = createCourse(newCourseRequest);
        Course saved = courseRepository.save(course);
        return createResponse(saved);
    }

    public CourseNameResponse getCourseName(Long courseId) {
        Course courses = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        return CourseNameResponse.builder()
                .course_name(courses.getCourse_name())
                .build();

    }

    private Course createCourse(NewCourseRequest newCourseRequest) {
        return Course.builder()
                .course_name(newCourseRequest.getCourse_name())
                .course_category(newCourseRequest.getCourse_category())
                .course_credit(newCourseRequest.getCourse_credit())
                .isYouguan(newCourseRequest.getIsYouguan())
                .kaikeYuanxi(newCourseRequest.getKaikeYuanxi())
                .build();
    }

    private NewCourseResponse createResponse(Course course) {
        return NewCourseResponse.builder()
                .course_id(course.getId())
                .course_name(course.getCourse_name())
                .course_category(course.getCourse_category())
                .course_credit(course.getCourse_credit())
                .isYouguan(course.getIsYouguan())
                .kaikeYuanxi(course.getKaikeYuanxi())
                .build();
    }

}
