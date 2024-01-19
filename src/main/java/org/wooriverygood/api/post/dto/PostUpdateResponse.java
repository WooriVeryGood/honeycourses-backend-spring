package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateResponse {

    private final Long post_id;
    private final String post_title;
    private final String post_content;


    @Builder
    public PostUpdateResponse(Long post_id, String post_title, String post_content) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
    }
}
