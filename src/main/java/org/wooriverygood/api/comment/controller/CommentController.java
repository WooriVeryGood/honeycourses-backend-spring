package org.wooriverygood.api.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.comment.dto.CommentResponse;
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

}
