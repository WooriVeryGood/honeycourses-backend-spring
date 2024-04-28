package org.wooriverygood.api.course.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.dto.CourseNameResponse;
import org.wooriverygood.api.course.dto.CoursesResponse;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CourseFindServiceTest extends MockTest {

    @InjectMocks
    private CourseFindService courseFindService;

    @Mock
    private CourseRepository courseRepository;

    private Course course = Course.builder()
            .id(3L)
            .name("Gaoshu Test")
            .category("Zhuanye")
            .credit(5)
            .isYouguan(0)
            .kaikeYuanxi("Xinke")
            .reviewCount(0)
            .build();

    private List<Course> courses = new ArrayList<>();

    private final int COURSES_COUNT = 10;


    @BeforeEach
    void setup() {
        for (long i = 1; i <= COURSES_COUNT; i++) {
            courses.add(Course.builder()
                    .id(i)
                    .name("Course" + i)
                    .category("Zhuanye")
                    .credit(5)
                    .isYouguan(0)
                    .kaikeYuanxi("Xinke")
                    .reviewCount(0)
                    .build());
        }
    }

    @Test
    @DisplayName("강의 전체 조회")
    void getCourses() {
        when(courseRepository.findAll()).thenReturn(courses);

        CoursesResponse response = courseFindService.findAll();

        Assertions.assertThat(response.getCourses().size()).isEqualTo(COURSES_COUNT);
    }

    @Test
    @DisplayName("특정 강의의 이름을 조회온다.")
    void getCourseName() {
        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(course));

        CourseNameResponse response = courseFindService.findCourseName(3L);

        Assertions.assertThat(response.getCourseName()).isEqualTo(course.getName());
    }

}