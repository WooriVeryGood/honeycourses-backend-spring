package org.wooriverygood.api.comment.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.comment.exception.ReplyDepthException;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.ApiTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

class CommentApiTest extends ApiTest {

    private List<CommentResponse> responses = new ArrayList<>();

    private Post post = Post.builder()
            .id(1L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .member(new Member(1L, "username"))
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    @BeforeEach
    void setUp() {
        for (long i = 1; i <= 4; i++) {
            responses.add(CommentResponse.builder()
                    .commentId(i)
                    .commentContent("content" + i)
                    .postId(post.getId())
                    .commentLikeCount((int) i + 8)
                    .commentTime(LocalDateTime.now())
                    .liked(i % 3 == 0)
                    .replies(new ArrayList<>())
                    .updated(i % 2 == 0)
                    .reported(false)
                    .memberId(1L)
                    .isMine(true)
                    .build());
        }
        for (int i = 12; i <= 15; i++) {
            responses.get(2).getReplies()
                    .add(ReplyResponse.builder()
                            .replyId((long) i)
                            .replyContent("reply content " + i)
                            .memberId(1L)
                            .isMine(true)
                            .replyLikeCount(i - 6)
                            .replyTime(LocalDateTime.now())
                            .liked(false)
                            .updated(i % 2 == 0)
                            .reported(false)
                            .build());
        }
    }

    @Test
    @DisplayName("특정 게시글의 댓글 조회 요청을 받으면 댓글들을 반환한다.")
    void findAllCommentsByPostId() {
        when(commentFindService.findAllCommentsByPostId(anyLong(), any(AuthInfo.class)))
                .thenReturn(new CommentsResponse(responses));

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().get("/posts/1/comments")
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

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().post("/posts/51/comments")
                .then().log().all()
                .assertThat()
                .apply(document("comments/create/success"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 올리거나 내린다.")
    void likeComment() {
        when(commentLikeToggleService.likeComment(any(Long.class), any(AuthInfo.class)))
                .thenReturn(CommentLikeResponse.builder()
                        .likeCount(5)
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

        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .body(request)
                .when().put("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/update/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
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

        doThrow(new AuthorizationException())
                .when(commentUpdateService)
                .updateComment(anyLong(), any(CommentUpdateRequest.class), any(AuthInfo.class));

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
        restDocs
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer aws-cognito-access-token")
                .when().delete("/comments/3")
                .then().log().all()
                .assertThat()
                .apply(document("comments/delete/success"))
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("권한이 없는 댓글을 삭제하면 404를 반환한다.")
    void deleteComment_exception_noAuth() {
        doThrow(new AuthorizationException())
                .when(commentDeleteService)
                .deleteComment(anyLong(), any(AuthInfo.class));

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
    @DisplayName("특정 대댓글의 대댓글을 작성하려고 하면, 400 에러를 반환한다.")
    void addReply_exception_depth() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();

        doThrow(new ReplyDepthException())
                .when(commentCreateService)
                .addReply(anyLong(), any(NewReplyRequest.class), any(AuthInfo.class));

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