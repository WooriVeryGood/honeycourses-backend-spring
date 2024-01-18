package org.wooriverygood.api.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.dto.PostLikeResponse;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.util.ControllerTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


class PostControllerTest extends ControllerTest {

    private final int POST_RESPONSES_COUNT = 10;
    private List<PostResponse> responses = new ArrayList<>();


    @BeforeEach
    void setUp() {
        for (int i = 1; i < POST_RESPONSES_COUNT; i++) {
            responses.add(PostResponse.builder()
                    .post_id((long) i)
                    .post_title("title" + i)
                    .post_category("자유")
                    .post_content("content" + i)
                    .post_comments(2)
                    .post_likes(3)
                    .post_time(LocalDateTime.now())
                    .build());
        }
    }


    @Test
    @DisplayName("게시글 전부 반환한다.")
    void findAllPosts() {
        Mockito.when(postService.findAllPosts(AuthInfo.builder().build()))
                .thenReturn(responses);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/all/success"))
                .statusCode(HttpStatus.OK.value());
    }

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

        Mockito.when(postService.findPostById(any(Long.class), any(AuthInfo.class)))
                .thenReturn(response);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community/1")
                .then().log().all()
                .assertThat()
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
                .assertThat()
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
                .assertThat()
                .apply(document("post/create/fail/noTitle"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("사용자 본인이 작성한 게시글을 불러온다.")
    void findMyPosts() {
        List<PostResponse> responses = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            responses.add(PostResponse.builder()
                    .post_id(1L)
                    .post_title("title")
                    .post_category("자유")
                    .post_content("content")
                    .post_comments(0)
                    .post_likes(0)
                    .post_time(LocalDateTime.now())
                    .isMine(true)
                    .build());
        }

        Mockito.when(postService.findMyPosts(any(AuthInfo.class)))
                .thenReturn(responses);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community/me")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/me/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 게시글의 좋아요를 1 올리거나 내린다.")
    void likePost_up() {
        Mockito.when(postService.likePost(any(Long.class), any(AuthInfo.class)))
                .thenReturn(PostLikeResponse.builder().build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().put("/community/1/like")
                .then().log().all()
                .assertThat()
                .apply(document("post/like/success"))
                .statusCode(HttpStatus.OK.value());
    }
}