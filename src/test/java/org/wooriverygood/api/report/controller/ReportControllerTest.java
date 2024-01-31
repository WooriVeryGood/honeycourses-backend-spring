package org.wooriverygood.api.report.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.util.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class ReportControllerTest extends ControllerTest {

    @Test
    @DisplayName("특정 게시글을 신고하면 201을 반환한다.")
    void reportPost() {
        ReportRequest request = ReportRequest.builder()
                .message("신고 내용")
                .build();

        doNothing().when(reportService)
                .reportPost(any(Long.class), any(ReportRequest.class), any(AuthInfo.class));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/posts/1/report")
                .then().log().all()
                .assertThat()
                .apply(document("posts/report/success"))
                .statusCode(HttpStatus.CREATED.value());
    }
}

