package org.wooriverygood.api.util;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.wooriverygood.api.comment.controller.CommentController;
import org.wooriverygood.api.comment.service.CommentService;
import org.wooriverygood.api.course.application.CourseFindService;
import org.wooriverygood.api.post.application.*;
import org.wooriverygood.api.report.api.ReportApi;
import org.wooriverygood.api.report.application.CommentReportService;
import org.wooriverygood.api.course.api.CourseApi;
import org.wooriverygood.api.course.application.CourseCreateService;
import org.wooriverygood.api.post.api.PostApi;
import org.wooriverygood.api.report.application.PostReportService;
import org.wooriverygood.api.review.api.ReviewApi;
import org.wooriverygood.api.review.application.*;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.auth.AuthenticationPrincipalArgumentResolver;


import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest({
        CommentController.class,
        PostApi.class,
        CourseApi.class,
        ReviewApi.class,
        ReportApi.class
})
@WithMockUser
@ExtendWith(RestDocumentationExtension.class)
public class ApiTest {

    protected MockMvcRequestSpecification restDocs;

    @MockBean
    protected CourseCreateService courseCreateService;

    @MockBean
    protected CourseFindService courseFindService;

    @MockBean
    protected ReviewLikeToggleService reviewLikeToggleService;

    @MockBean
    protected ReviewFindService reviewFindService;

    @MockBean
    protected ReviewCreateService reviewCreateService;

    @MockBean
    protected ReviewDeleteService reviewDeleteService;

    @MockBean
    protected ReviewUpdateService reviewUpdateService;

    @MockBean
    protected ReviewValidateAccessService reviewValidateAccessService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected PostLikeToggleService postLikeToggleService;

    @MockBean
    protected PostFindService postFindService;

    @MockBean
    protected PostCreateService postCreateService;

    @MockBean
    protected PostUpdateService postUpdateService;

    @MockBean
    protected PostDeleteService postDeleteService;

    @MockBean
    protected CommentReportService commentReportService;

    @MockBean
    protected PostReportService postReportService;

    @MockBean
    protected AuthenticationPrincipalArgumentResolver authenticationPrincipalArgumentResolver;

    protected AuthInfo testAuthInfo;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        restDocs = RestAssuredMockMvc.given()
                .mockMvc(MockMvcBuilders.webAppContextSetup(webApplicationContext)
                        .apply(springSecurity())
                        .defaultRequest(post("/**").with(csrf().asHeader()))
                        .defaultRequest(delete("/**").with(csrf().asHeader()))
                        .defaultRequest(put("/**").with(csrf().asHeader()))
                        .apply(documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint(), modifyHeaders().remove("X-CSRF-TOKEN"))  // 명세서에 불필요한 헤더는 제거
                                .withResponseDefaults(prettyPrint(), modifyHeaders()
                                        .remove("X-Content-Type-Options")
                                        .remove("X-XSS-Protection")
                                        .remove("Pragma")
                                        .remove("X-Frame-Options")))
                        .build())
                .log().all();

        testAuthInfo = AuthInfo.builder()
                .sub("22432-12312-3531")
                .username("22432-12312-3531")
                .build();
    }
}
