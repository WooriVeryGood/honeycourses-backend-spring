package org.wooriverygood.api.post.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.dto.NewPostResponse;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.service.PostService;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.support.Login;

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

    @PostMapping
    public ResponseEntity<NewPostResponse> addPost(@Login AuthInfo authInfo,
                                                   @RequestBody NewPostRequest newPostRequest) {
        NewPostResponse response = postService.addPost(authInfo, newPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
