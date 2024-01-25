package org.wooriverygood.api.post.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.post.dto.*;
import org.wooriverygood.api.post.service.PostService;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.support.Login;


@RestController
@RequestMapping("/community")
public class PostController {

    private final PostService postService;

    private final int PAGE_SIZE = 10;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PostsResponse> findPosts(@Login AuthInfo authInfo,
                                                   @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        PostsResponse response = postService.findPosts(authInfo, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<PostsResponse> findPostsByCategory(@Login AuthInfo authInfo,
                                                             @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                             @PathVariable("category") String postCategory) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        PostsResponse response = postService.findPostsByCategory(authInfo, pageable, postCategory);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findPostById(@PathVariable("id") Long postId,
                                                     @Login AuthInfo authInfo) {
        PostResponse postResponse = postService.findPostById(postId, authInfo);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping
    public ResponseEntity<NewPostResponse> addPost(@Login AuthInfo authInfo,
                                                   @Valid @RequestBody NewPostRequest newPostRequest) {
        NewPostResponse response = postService.addPost(authInfo, newPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<PostsResponse> findMyPosts(@Login AuthInfo authInfo,
                                                     @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        PostsResponse response = postService.findMyPosts(authInfo, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable("id") Long postId,
                                                     @Login AuthInfo authInfo) {
        PostLikeResponse response = postService.likePost(postId, authInfo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostUpdateResponse> updatePost(@PathVariable("id") Long postId,
                                                         @Valid @RequestBody PostUpdateRequest postUpdateRequest,
                                                         @Login AuthInfo authInfo) {
        PostUpdateResponse response = postService.updatePost(postId, postUpdateRequest, authInfo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostDeleteResponse> deletePost(@PathVariable("id") Long postId,
                                                         @Login AuthInfo authInfo) {
        PostDeleteResponse response = postService.deletePost(postId, authInfo);
        return ResponseEntity.ok(response);
    }
}
