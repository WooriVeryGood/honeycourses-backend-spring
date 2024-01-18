package org.wooriverygood.api.course.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class CourseServiceTest {
    @MockBean
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;

    private List<Courses> testCourses = new ArrayList<>();

    private final int COURSES_COUNT = 10;

    @BeforeEach
    void setup() {
        for(int i = 0; i<COURSES_COUNT; i++) {
            testCourses.add(Courses.builder()
                    .course_name("Gaoshu" + i)
                    .course_category("Zhuanye")
                    .course_credit(5)
                    .isYouguan(0)
                    .kaikeYuanxi("Xinke")
                    .build());
        }
        when(courseRepository.findAll()).thenReturn(testCourses);
    }

    @Test
    @DisplayName("강의 목록 조회")
    void getCourses() {
        Mockito.when(courseRepository.findAll()).thenReturn(testCourses);

        List<CourseResponse> responses = courseService.findAll();

        // Then
        Assertions.assertThat(responses.size()).isEqualTo(COURSES_COUNT);

    }

}
