package org.wooriverygood.api.review.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.review.exception.ReviewAccessDeniedException;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.ApiTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

public class ReviewApiTest extends ApiTest {

    private List<ReviewResponse> responses = new ArrayList<>();

    private Course course = Course.builder()
            .id(1L)
            .name("Gaoshu")
            .category("Zhuanye")
            .credit(5)
            .isYouguan(0)
            .kaikeYuanxi("Xinke")
            .build();

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();


    @BeforeEach
    void setUp() {
        for(int i = 1; i <= 2; i++) {
            responses.add(ReviewResponse.builder()
                    .reviewId((long)i)
                    .courseId(1L)
                    .reviewContent("Test Review " + i)
                    .reviewTitle("Title" + i)
                    .instructorName("Jiaoshou")
                    .takenSemyr("22-23")
                    .grade("60")
                    .reviewTime(LocalDateTime.now())
                    .isMine(false)
                    .updated(false)
                    .build());
        }
    }

    @Test
    @DisplayName("최근 작성한 리뷰가 6개월 미만이라면, 특정 강의의 리뷰 조회 요청을 받으면 리뷰들을 반환한다.")
    void findAllReviewsByCourseId_success() {
        doNothing()
                .when(reviewValidateAccessService)
                .validateReviewAccess(any(AuthInfo.class));
        when(reviewFindService.findAllReviewsByCourseId(anyLong(), any(AuthInfo.class)))
                .thenReturn(new ReviewsResponse(responses));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/courses/1/reviews")
                .then().log().all()
                .apply(document("reviews/find/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("최근 작성한 리뷰가 6개월 이상이거나 없다면, 리뷰 요청에 대한 BadRequest 예외를 반환한다.")
    void findAllReviewsByCourseId_exception_accessDenied() {
        doThrow(new ReviewAccessDeniedException())
                .when(reviewValidateAccessService)
                .validateReviewAccess(any(AuthInfo.class));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/courses/1/reviews")
                .then().log().all()
                .apply(document("reviews/find/fail/denied"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("특정 강의의 리뷰를 작성한다.")
    void addReview() {
        NewReviewRequest request = NewReviewRequest.builder()
                .reviewTitle("Test Review from TestCode")
                .instructorName("Jiaoshou")
                .takenSemyr("1stSem")
                .reviewContent("Good!")
                .grade("100")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/courses/50/reviews")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("특정 리뷰의 좋아요를 1 올리거나 내린다.")
    void likeReview() {
        when(reviewLikeToggleService.toggleReviewLike(any(Long.class), any(AuthInfo.class)))
                .thenReturn(ReviewLikeResponse.builder()
                        .likeCount(5)
                        .liked(false)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().put("/reviews/1/like")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/like/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("사용자 본인이 작성한 리뷰들을 불러온다.")
    void findMyReviews() {
        List<ReviewResponse> responses = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            responses.add(ReviewResponse.builder()
                    .reviewId((long)i)
                    .courseId(1L)
                    .reviewContent("Test Review " + i)
                    .reviewTitle("Title" + i)
                    .instructorName("Jiaoshou")
                    .takenSemyr("22-23")
                    .grade("60")
                    .reviewTime(LocalDateTime.now())
                    .isMine(true)
                    .updated(false)
                    .build());
        }

        when(reviewFindService.findMyReviews(any(AuthInfo.class)))
                .thenReturn(new ReviewsResponse(responses));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/reviews/me")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/find/me/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 있는 리뷰를 수정한다.")
    void updateReview() {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .reviewTitle("new title")
                .reviewContent("new content")
                .instructorName("jiaoshou")
                .takenSemyr("18-19")
                .grade("100")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/reviews/1")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/update/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("권한이 없는 리뷰를 수정하면 403을 반환한다.")
    void updateReview_noAuth() {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .reviewTitle("new title")
                .reviewContent("new content")
                .build();

        doThrow(new AuthorizationException())
                .when(reviewUpdateService)
                .updateReview(anyLong(), any(ReviewUpdateRequest.class), any(AuthInfo.class));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/reviews/1")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/update/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("권한이 있는 리뷰를 삭제한다.")
    void deleteReview() {
        restDocs
                .header("Authorization", "any")
                .when().delete("/reviews/8")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/delete/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("권한이 없는 리뷰를 삭제하면 403을 반환한다.")
    void deleteReview_noAuth() {
        doThrow(new AuthorizationException())
                .when(reviewDeleteService)
                .deleteReview(anyLong(), any(AuthInfo.class));

        restDocs
                .header("Authorization", "any")
                .when().delete("/reviews/8")
                .then().log().all()
                .assertThat()
                .apply(document("reviews/delete/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}
