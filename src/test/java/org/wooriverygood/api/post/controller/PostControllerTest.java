package org.wooriverygood.api.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.advice.exception.PostNotFoundException;
import org.wooriverygood.api.post.dto.*;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.util.ControllerTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


class PostControllerTest extends ControllerTest {

    PostsResponse response;
    List<PostResponse> responses = new ArrayList<>();

    AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();


    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 23; i++) {
            responses.add(PostResponse.builder()
                    .post_id((long) i)
                    .post_title("title" + i)
                    .post_category("자유")
                    .post_content("content" + i)
                    .post_author(authInfo.getUsername())
                    .post_comments(10 + i)
                    .post_likes(2 + i)
                    .post_time(LocalDateTime.now())
                    .liked(i % 5 == 0)
                    .updated(i % 2 == 0)
                    .build());
        }
        response = PostsResponse.builder()
                .posts(responses.stream().filter(it -> it.getPost_id() >= 4 && it.getPost_id() < 14).toList())
                .totalPostCount(23)
                .totalPageCount(3)
                .build();
    }

    @Test
    @DisplayName("게시글을 전부 반환한다.")
    void findPosts() {
        Mockito.when(postService.findPosts(any(AuthInfo.class), any(Pageable.class)))
                .thenReturn(response);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community?page=1")
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
                .post_author(authInfo.getUsername())
                .post_comments(2)
                .post_likes(3)
                .post_time(LocalDateTime.now())
                .liked(false)
                .updated(false)
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
    @DisplayName("유효하지 않은 id로 게시글을 조회하면 404를 반환한다.")
    void findPost_exception_invalidId() {
        Mockito.when(postService.findPostById(any(Long.class), any(AuthInfo.class)))
                        .thenThrow(new PostNotFoundException());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/one/fail"))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("새로운 게시글을 성공적으로 등록한다.")
    public void addPost() {
        NewPostRequest request = NewPostRequest.builder()
                .post_title("title")
                .post_category("자유")
                .post_content("content")
                .build();

        Mockito.when(postService.addPost(any(AuthInfo.class), any(NewPostRequest.class)))
                .thenReturn(NewPostResponse.builder()
                        .post_id(6L)
                        .title(request.getPost_title())
                        .category(request.getPost_category())
                        .author(authInfo.getUsername())
                        .build());

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
    @DisplayName("새로운 게시글의 제목이 없으면 400을 반환한다.")
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
                    .post_id((long) i)
                    .post_title("title")
                    .post_category("자유")
                    .post_content("content")
                    .post_author(authInfo.getUsername())
                    .post_comments(0)
                    .post_likes(0)
                    .post_time(LocalDateTime.now())
                    .liked(false)
                    .updated(false)
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
    void likePost() {
        Mockito.when(postService.likePost(any(Long.class), any(AuthInfo.class)))
                .thenReturn(PostLikeResponse.builder()
                        .like_count(5)
                        .liked(true)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().put("/community/1/like")
                .then().log().all()
                .assertThat()
                .apply(document("post/like/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 있는 게시글을 수정한다.")
    void updatePost() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .post_title("new title")
                .post_content("new content")
                .build();

        Mockito.when(postService.updatePost(any(Long.class), any(PostUpdateRequest.class), any(AuthInfo.class)))
                        .thenReturn(PostUpdateResponse.builder()
                                .post_id((long) 1)
                                .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/community/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/update/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 없는 게시글을 수정하면 403을 반환한다.")
    void updatePost_exception_noAuth() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .post_title("new title")
                .post_content("new content")
                .build();

        Mockito.when(postService.updatePost(any(Long.class), any(PostUpdateRequest.class), any(AuthInfo.class)))
                        .thenThrow(new AuthorizationException());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/community/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/update/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("게시물 수정 시, 제목에 내용이 없는 경우 400을 반환한다.")
    @Test
    void updatePost_exception_noTitle() {
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .post_content("content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(postUpdateRequest)
                .header("Authorization", "any")
                .when().put("/community/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/update/fail/noTitle"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("권한이 있는 게시글을 삭제한다.")
    void deletePost() {
        Mockito.when(postService.deletePost(any(Long.class), any(AuthInfo.class)))
                .thenReturn(PostDeleteResponse.builder()
                        .post_id(7L)
                        .build());

        restDocs
                .header("Authorization", "any")
                .when().delete("/community/7")
                .then().log().all()
                .assertThat()
                .apply(document("post/delete/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 없는 게시글을 삭제하면 403을 반환한다.")
    void deletePost_exception_forbidden() {
        Mockito.when(postService.deletePost(any(Long.class), any(AuthInfo.class)))
                .thenThrow(new AuthorizationException());

        restDocs
                .header("Authorization", "any")
                .when().delete("/community/7")
                .then().log().all()
                .assertThat()
                .apply(document("post/delete/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}