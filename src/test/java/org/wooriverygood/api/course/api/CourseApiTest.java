package org.wooriverygood.api.course.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.course.dto.CourseNameResponse;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.dto.CoursesResponse;
import org.wooriverygood.api.course.dto.NewCourseRequest;
import org.wooriverygood.api.util.ApiTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class CourseApiTest extends ApiTest {

    private List<CourseResponse> responses = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (long i = 0; i < 2; i++) {
            responses.add(CourseResponse.builder()
                    .courseId(i)
                    .courseCategory("Zhuanye")
                    .courseCredit(5)
                    .courseName("Gaoshu"+i)
                    .isYouguan(0)
                    .kaikeYuanxi("Xinke")
                    .reviewCount(0)
                    .build());
        }
    }

    @Test
    @DisplayName("모든 강의 조회 요청을 받으면 강의를 반환한다.")
    void getCourses() {
        when(courseFindService.findAll()).thenReturn(new CoursesResponse(responses));

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
                .courseName("테스트 강의")
                .courseCategory("전공")
                .courseCredit(5)
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

    @Test
    @DisplayName("특정 강의의 이름을 반환한다.")
    void getCourseName() {
        CourseNameResponse response = new CourseNameResponse("테스트 강의");

        when(courseFindService.findCourseName(anyLong()))
                .thenReturn(response);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/courses/1/name")
                .then().log().all()
                .assertThat()
                .apply(document("course/get/name/success"))
                .statusCode(HttpStatus.OK.value());
    }

}
