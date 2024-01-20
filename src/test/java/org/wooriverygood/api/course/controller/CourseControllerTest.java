package org.wooriverygood.api.course.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.util.ControllerTest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class CourseControllerTest extends ControllerTest {
    List<CourseResponse> responses = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 2; i++) {
            responses.add(CourseResponse.builder()
                    .course_id((long)i)
                    .course_category("Zhuanye")
                    .course_credit(5)
                    .course_name("Gaoshu"+i)
                    .isYouguan(0)
                    .kaikeYuanxi("Xinke")
                    .build());
        }
    }

    @Test
    @DisplayName("모든 강의 조회 요청을 받으면 강의를 반환한다.")
    void getCourses() {
        Mockito.when(courseService.findAll()).thenReturn(responses);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/courses")
                .then().log().all()
                .apply(document("courses/find/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("새로운 수업을 성공적으로 등록한다.")
    public void addCourse() {
        NewCourseRequest request = NewCourseRequest.builder()
                .course_name("테스트 강의")
                .course_category("전공")
                .course_credit(5)
                .kaikeYuanxi("씬커")
                .isYouguan(0)
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/courses")
                .then().log().all()
                .apply(document("courses/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }
}
