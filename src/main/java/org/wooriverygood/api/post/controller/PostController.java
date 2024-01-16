package org.wooriverygood.api.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/community")
public class PostController {

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAllPosts() {
        List<PostResponse> postResponses = postService.findAllPosts();
        return ResponseEntity.ok().body(postResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findPostById(@PathVariable("id") Long postId) {
        PostResponse postResponse = postService.findPostById(postId);
        return ResponseEntity.ok().body(postResponse);
    }
}
