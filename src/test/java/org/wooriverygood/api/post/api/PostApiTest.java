package org.wooriverygood.api.post.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.dto.*;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.ApiTest;
import org.wooriverygood.api.util.ResponseFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

class PostApiTest extends ApiTest {

    @Test
    @DisplayName("전체 게시글의 1번째 페이지를 반환한다.")
    void findPosts() {
        when(postFindService.findPosts(any(AuthInfo.class), any(Pageable.class), anyString()))
                .thenReturn(ResponseFixture.postsResponse(1, 39, testAuthInfo));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/posts?page=1")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/all/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("자유 카테고리의 2번째 페이지를 반환한다.")
    void findPostsByCategory() {
        List<PostResponse> responses = new ArrayList<>();
        int bound = Math.min(39 - 2 * 10 + 1, 10);
        int start = 2 * 10 + 1;
        for (long i = start; i < start + bound; i++)
            responses.add(PostResponse.builder()
                    .postId(i)
                    .postTitle("title_" + i)
                    .postCategory("자유")
                    .postComments((int) (Math.random() * 100))
                    .postLikes((int) (Math.random() * 100))
                    .postTime(LocalDateTime.now())
                    .liked(i % 6 == 0)
                    .updated(i % 9 == 0)
                    .reported(false)
                    .memberId(1L)
                    .isMine(false)
                    .build());

        when(postFindService.findPosts(any(AuthInfo.class), any(Pageable.class), anyString()))
                .thenReturn(PostsResponse.builder()
                        .posts(responses)
                        .totalPostCount(39)
                        .totalPageCount((int) Math.ceil((double) 39 / 10))
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/posts?page=2&category=free")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/category/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 게시글 조회 요청을 받으면 게시글을 반환한다.")
    void findPost() {
        when(postFindService.findPostById(anyLong(), any(AuthInfo.class)))
                .thenReturn(PostDetailResponse.builder()
                        .postId(1L)
                        .postTitle("title_1")
                        .postCategory("자유")
                        .postComments((int) (Math.random() * 100))
                        .postLikes((int) (Math.random() * 100))
                        .postTime(LocalDateTime.now())
                        .liked(false)
                        .updated(false)
                        .reported(false)
                        .memberId(1L)
                        .isMine(false)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/posts/4")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/one/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("유효하지 않은 id로 게시글을 조회하면 404를 반환한다.")
    void findPost_exception_invalidId() {
        when(postFindService.findPostById(anyLong(), any(AuthInfo.class)))
                .thenThrow(new PostNotFoundException());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/posts/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/one/fail"))
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("새로운 게시글을 성공적으로 등록한다.")
    public void addPost() {
        NewPostRequest request = NewPostRequest.builder()
                .postTitle("title")
                .postCategory("자유")
                .postContent("content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/posts")
                .then().log().all()
                .assertThat()
                .apply(document("post/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("새로운 게시글의 제목이 없으면 400을 반환한다.")
    public void addPost_exception_noTitle() {
        NewPostRequest request = NewPostRequest.builder()
                .postCategory("자유")
                .postContent("content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/posts")
                .then().log().all()
                .assertThat()
                .apply(document("post/create/fail/noTitle"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("사용자 본인이 작성한 게시글을 불러온다.")
    void findMyPosts() {
        when(postFindService.findMyPosts(any(AuthInfo.class), any(PageRequest.class)))
                .thenReturn(ResponseFixture.postsResponse(1, 14, testAuthInfo));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/posts/me?page=1")
                .then().log().all()
                .assertThat()
                .apply(document("post/find/me/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 게시글의 좋아요를 1 올리거나 내린다.")
    void likePost() {
        when(postLikeToggleService.togglePostLike(anyLong(), any(AuthInfo.class)))
                .thenReturn(PostLikeResponse.builder()
                        .likeCount(5)
                        .liked(true)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().put("/posts/1/like")
                .then().log().all()
                .assertThat()
                .apply(document("post/like/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 있는 게시글을 수정한다.")
    void updatePost() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .postTitle("new title")
                .postContent("new content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/posts/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/update/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("권한이 없는 게시글을 수정하면 403을 반환한다.")
    void updatePost_exception_noAuth() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .postTitle("new title")
                .postContent("new content")
                .build();

        doThrow(new AuthorizationException())
                .when(postUpdateService)
                .updatePost(anyLong(), any(PostUpdateRequest.class), any(AuthInfo.class));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/posts/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/update/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("게시물 수정 시, 제목에 내용이 없는 경우 400을 반환한다.")
    @Test
    void updatePost_exception_noTitle() {
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder()
                .postContent("content")
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(postUpdateRequest)
                .header("Authorization", "any")
                .when().put("/posts/1")
                .then().log().all()
                .assertThat()
                .apply(document("post/update/fail/noTitle"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("권한이 있는 게시글을 삭제한다.")
    void deletePost() {
        restDocs
                .header("Authorization", "any")
                .when().delete("/posts/7")
                .then().log().all()
                .assertThat()
                .apply(document("post/delete/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("권한이 없는 게시글을 삭제하면 403을 반환한다.")
    void deletePost_exception_forbidden() {
        doThrow(new AuthorizationException())
                .when(postDeleteService)
                .deletePost(any(AuthInfo.class), anyLong());

        restDocs
                .header("Authorization", "any")
                .when().delete("/posts/7")
                .then().log().all()
                .assertThat()
                .apply(document("post/delete/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

}