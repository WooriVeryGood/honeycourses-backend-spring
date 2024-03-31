package org.wooriverygood.api.post.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wooriverygood.api.post.application.*;
import org.wooriverygood.api.post.dto.*;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.auth.Login;


@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostApi {

    private final PostLikeToggleService postLikeToggleService;

    private final PostFindService postFindService;

    private final PostCreateService postCreateService;

    private final PostDeleteService postDeleteService;

    private final PostUpdateService postUpdateService;

    private final int PAGE_SIZE = 10;


    @GetMapping
    public ResponseEntity<PostsResponse> findPosts(@Login AuthInfo authInfo,
                                                   @RequestParam(required = false, defaultValue = "", value = "category") String category,
                                                   @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        PostsResponse response = postFindService.findPosts(authInfo, pageable, category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findPostById(@PathVariable("id") Long postId,
                                                     @Login AuthInfo authInfo) {
        PostResponse postResponse = postFindService.findPostById(postId, authInfo);
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping
    public ResponseEntity<Void> addPost(@Login AuthInfo authInfo,
                                        @Valid @RequestBody NewPostRequest newPostRequest) {
        postCreateService.addPost(authInfo, newPostRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/me")
    public ResponseEntity<PostsResponse> findMyPosts(@Login AuthInfo authInfo,
                                                     @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE);
        PostsResponse response = postFindService.findMyPosts(authInfo, pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<PostLikeResponse> togglePostLike(@PathVariable("id") long postId,
                                                     @Login AuthInfo authInfo) {
        PostLikeResponse response = postLikeToggleService.togglePostLike(postId, authInfo);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable("id") long postId,
                                                         @Valid @RequestBody PostUpdateRequest postUpdateRequest,
                                                         @Login AuthInfo authInfo) {
        postUpdateService.updatePost(postId, postUpdateRequest, authInfo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") long postId,
                                           @Login AuthInfo authInfo) {
        postDeleteService.deletePost(authInfo, postId);
        return ResponseEntity.noContent().build();
    }

}
