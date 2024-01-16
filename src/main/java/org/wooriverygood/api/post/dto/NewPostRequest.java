package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostRequest {

    private final String post_title;
    private final String post_category;
    private final String post_content;
    private final String email;

    @Builder
    public NewPostRequest(String post_title, String post_category, String post_content, String email) {
        this.post_title = post_title;
        this.post_category = post_category;
        this.post_content = post_content;
        this.email = email;
    }
}
