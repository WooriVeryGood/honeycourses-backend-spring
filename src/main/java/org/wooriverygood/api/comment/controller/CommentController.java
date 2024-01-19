package org.wooriverygood.api.comment.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.comment.dto.CommentLikeResponse;
import org.wooriverygood.api.comment.dto.CommentResponse;
import org.wooriverygood.api.comment.dto.NewCommentRequest;
import org.wooriverygood.api.comment.dto.NewCommentResponse;
import org.wooriverygood.api.comment.service.CommentService;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.support.Login;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/community/{id}/comments")
    public ResponseEntity<List<CommentResponse>> findAllCommentsByPostId(@PathVariable("id") Long postId) {
        List<CommentResponse> responses = commentService.findAllCommentsByPostId(postId);
        return  ResponseEntity.ok(responses);
    }

    @PostMapping("/community/{id}/comments")
    public ResponseEntity<NewCommentResponse> addComment(@PathVariable("id") Long postId,
                                                         @Login AuthInfo authInfo,
                                                         @Valid @RequestBody NewCommentRequest newCommentRequest) {
        NewCommentResponse response = commentService.addComment(authInfo, postId, newCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/community/comments/{id}/like")
    public ResponseEntity<CommentLikeResponse> likeComment(@PathVariable("id") Long commentId,
                                                           @Login AuthInfo authInfo) {
        CommentLikeResponse response = commentService.likeComment(commentId, authInfo);
        return ResponseEntity.ok().body(response);
    }

}
