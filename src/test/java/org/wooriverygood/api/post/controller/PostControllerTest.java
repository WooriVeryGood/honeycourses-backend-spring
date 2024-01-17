package org.wooriverygood.api.post.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.util.ControllerTest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


class PostControllerTest extends ControllerTest {

    @Test
    @DisplayName("특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
    void findPost() {
        PostResponse response = PostResponse.builder()
                .post_id(1L)
                .post_title("title")
                .post_category("자유")
                .post_content("content")
                .post_comments(2)
                .post_likes(3)
                .post_time(LocalDateTime.now())
                .build();

        Mockito.when(postService.findPostById(any(Long.class)))
                        .thenReturn(response);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community/1")
                .then().log().all()
                .apply(document("post/find/one/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("새로운 게시글을 성공적으로 등록한다.")
    public void addPost() {
        NewPostRequest request = NewPostRequest.builder()
                .post_title("title")
                .post_category("자유")
                .post_content("content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/community")
                .then().log().all()
                .apply(document("post/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("새로운 게시글의 제목이 없으면 등록에 실패한다.")
    public void addPost_exception_noTitle() {
        NewPostRequest request = NewPostRequest.builder()
                .post_category("자유")
                .post_content("content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/community")
                .then().log().all()
                .apply(document("post/create/fail/noTitle"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}