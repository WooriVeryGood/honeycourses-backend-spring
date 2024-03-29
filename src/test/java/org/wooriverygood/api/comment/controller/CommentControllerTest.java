package org.wooriverygood.api.comment.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.advice.exception.ReplyDepthException;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.util.ControllerTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;


class CommentControllerTest extends ControllerTest {

    List<CommentResponse> responses = new ArrayList<>();

    Post post = Post.builder()
            .id(1L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author("user-3333")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 4; i++) {
            responses.add(CommentResponse.builder()
                    .comment_id((long) i)
                    .comment_content("content" + i)
                    .comment_author("user-"+(i % 5))
                    .post_id(post.getId())
                    .comment_likes(i + 8)
                    .comment_time(LocalDateTime.now())
                    .liked(i % 3 == 0)
                    .replies(new ArrayList<>())
                    .updated(i % 2 == 0)
                    .reported(false)
                    .build());
        }
        for (int i = 12; i <= 15; i++) {
            responses.get(2).getReplies()
                    .add(ReplyResponse.builder()
                            .reply_id((long) i)
                            .reply_content("reply content " + i)
                            .reply_author("user-" + (i % 2))
                            .reply_likes(i - 6)
                            .reply_time(LocalDateTime.now())
                            .liked(false)
                            .updated(i % 2 == 0)
                            .reported(false)
                            .build());
        }
    }

    @Test
    @DisplayName("특정 게시글의 댓글 조회 요청을 받으면 댓글들을 반환한다.")
    void findAllCommentsByPostId() {
        Mockito.when(commentService.findAllComments(any(Long.class), any(AuthInfo.class)))
                .thenReturn(responses);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/community/1/comments")
                .then().log().all()
                .assertThat()
                .apply(document("comments/find/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("특정 게시글의 댓글을 작성한다.")
    void addComment() {
        NewCommentRequest request = NewCommentRequest.builder()
                .content("content51")
                .build();

        NewCommentResponse response = NewCommentResponse.builder()
                .comment_id(51L)
                .content("content51")
                .author(testAuthInfo.getUsername())
                .build();

        Mockito.when(commentService.addComment(any(AuthInfo.class), any(Long.class), any(NewCommentRequest.class)))
                .thenReturn(response);

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/community/51/comments")
                .then().log().all()
                .assertThat()
                .apply(document("comments/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 올리거나 내린다.")
    void likeComment() {
        Mockito.when(commentService.likeComment(any(Long.class), any(AuthInfo.class)))
                .thenReturn(CommentLikeResponse.builder()
                        .like_count(5)
                        .liked(false)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().put("/comments/3/like")
                .then().log().all()
                .assertThat()
                .apply(document("comments/like/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 있는 댓글을 수정한다.")
    void updateComment() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("new comment content")
                .build();

        Mockito.when(commentService.updateComment(any(Long.class), any(CommentUpdateRequest.class), any(AuthInfo.class)))
                .thenReturn(CommentUpdateResponse.builder()
                        .comment_id(2L)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/update/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("수정하는 댓글의 내용이 없으면 400을 반환한다.")
    void updateComment_exception_noContent() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .build();

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/update/fail/noContent"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("권한이 없는 댓글을 수정하면 403을 반환한다.")
    void updateComment_exception_noAuth() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("new comment content")
                .build();

        Mockito.when(commentService.updateComment(any(Long.class), any(CommentUpdateRequest.class), any(AuthInfo.class)))
                .thenThrow(new AuthorizationException());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/update/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("권한이 있는 댓글을 삭제한다.")
    void deleteComment() {
        Mockito.when(commentService.deleteComment(any(Long.class), any(AuthInfo.class)))
                .thenReturn(CommentDeleteResponse.builder()
                        .comment_id(3L)
                        .build());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().delete("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/delete/success"))
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("권한이 없는 댓글을 삭제하면 404를 반환한다.")
    void deleteComment_exception_noAuth() {
        Mockito.when(commentService.deleteComment(any(Long.class), any(AuthInfo.class)))
                .thenThrow(new AuthorizationException());

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().delete("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/delete/fail/noAuth"))
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("특정 댓글의 대댓글을 작성한다.")
    void addReply() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();

        doNothing().when(commentService)
                .addReply(any(Long.class), any(NewReplyRequest.class), any(AuthInfo.class));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/comments/3/reply")
                .then().log().all()
                .assertThat()
                .apply(document("reply/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("특정 댓글의 대댓글을 작성한다.")
    void addReply_exception_depth() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();

        doThrow(new ReplyDepthException()).when(commentService)
                .addReply(any(Long.class), any(NewReplyRequest.class), any(AuthInfo.class));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/comments/3/reply")
                .then().log().all()
                .assertThat()
                .apply(document("reply/create/fail/depth"))
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}