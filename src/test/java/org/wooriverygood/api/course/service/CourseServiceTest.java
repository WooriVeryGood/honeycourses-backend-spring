package org.wooriverygood.api.course.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Courses;
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

    @BeforeEach
    void setup() {
        for(int i = 0; i<3; i++) {
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
        // When
        List<Courses> result = courseService.findAll();

        // Then
        Assertions.assertThat(result).hasSize(3);
        Assertions.assertThat(result).isEqualTo(testCourses);
        Assertions.assertThat(result.get(0).getCourse_name()).isEqualTo("Gaoshu0");

    }

}
