package org.wooriverygood.api.course.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.util.MockTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class CourseCreateServiceTest extends MockTest {

    @InjectMocks
    private CourseCreateService courseCreateService;

    @Mock
    private CourseRepository courseRepository;


    @Test
    @DisplayName("새로운 강의를 추가한다.")
    void addCourse() {
        NewCourseRequest request = NewCourseRequest.builder()
                .courseName("테스트 강의")
                .courseCategory("전공")
                .courseCredit(5)
                .kaikeYuanxi("씬커")
                .isYouguan(0)
                .build();

        courseCreateService.addCourse(request);

        verify(courseRepository).save(any(Course.class));
    }

}
