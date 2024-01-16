package org.wooriverygood.api.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.comment.dto.CommentResponse;
import org.wooriverygood.api.comment.dto.NewCommentRequest;
import org.wooriverygood.api.comment.dto.NewCommentResponse;
import org.wooriverygood.api.comment.service.CommentService;

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

    @PostMapping("/community/{id}/comment")
    public ResponseEntity<NewCommentResponse> addComment(@PathVariable("id") Long postId, @RequestBody NewCommentRequest newCommentRequest) {
        NewCommentResponse response = commentService.addComment(postId, newCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
