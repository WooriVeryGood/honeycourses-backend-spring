package org.wooriverygood.api.post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.service.CommunityService;

import java.util.List;

@RestController
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;


    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> findAllPosts() {
        List<PostResponse> postResponses = communityService.findAllPosts();
        return ResponseEntity.ok().body(postResponses);
    }
}
