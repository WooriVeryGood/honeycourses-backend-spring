package org.wooriverygood.api.course.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.course.repository.CourseRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseCreateService {

    private final CourseRepository courseRepository;


    @Transactional
    public void addCourse(NewCourseRequest request) {
        Course course = createCourse(request);
        courseRepository.save(course);
    }

    private Course createCourse(NewCourseRequest request) {
        return Course.builder()
                .name(request.getCourseName())
                .category(request.getCourseCategory())
                .credit(request.getCourseCredit())
                .isYouguan(request.getIsYouguan())
                .kaikeYuanxi(request.getKaikeYuanxi())
                .build();
    }

}
