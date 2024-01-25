package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostsResponse {

    private final List<PostResponse> posts;

    private final int totalPageCount;

    private final long totalPostCount;


    @Builder
    public PostsResponse(List<PostResponse> posts, int totalPageCount, long totalPostCount) {
        this.posts = posts;
        this.totalPageCount = totalPageCount;
        this.totalPostCount = totalPostCount;
    }

}
