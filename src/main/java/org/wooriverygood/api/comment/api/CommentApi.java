package org.wooriverygood.api.comment.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.comment.application.*;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.auth.Login;

@RestController
@RequiredArgsConstructor
public class CommentApi {

    private final CommentLikeToggleService commentLikeToggleService;

    private final CommentFindService commentFindService;

    private final CommentCreateService commentCreateService;

    private final CommentDeleteService commentDeleteService;

    private final CommentUpdateService commentUpdateService;


    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<CommentsResponse> findAllCommentsByPostId(@PathVariable("id") Long postId,
                                                                    @Login AuthInfo authInfo) {
        CommentsResponse response = commentFindService.findAllCommentsByPostId(postId, authInfo);
        return  ResponseEntity.ok(response);
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable("id") Long postId,
                                           @Login AuthInfo authInfo,
                                           @Valid @RequestBody NewCommentRequest newCommentRequest) {
        commentCreateService.addComment(authInfo, postId, newCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/comments/{id}/like")
    public ResponseEntity<CommentLikeResponse> toggleCommentLike(@PathVariable("id") Long commentId,
                                                                 @Login AuthInfo authInfo) {
        CommentLikeResponse response = commentLikeToggleService.likeComment(commentId, authInfo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable("id") Long commentId,
                                              @Valid @RequestBody CommentUpdateRequest request,
                                              @Login AuthInfo authInfo) {
        commentUpdateService.updateComment(commentId, request, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long commentId,
                                              @Login AuthInfo authInfo) {
        commentDeleteService.deleteComment(commentId, authInfo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/comments/{id}/reply")
    public ResponseEntity<Void> addReply(@PathVariable("id") Long commentId,
                                         @RequestBody NewReplyRequest request,
                                         @Login AuthInfo authInfo) {
        commentCreateService.addReply(commentId, request, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
