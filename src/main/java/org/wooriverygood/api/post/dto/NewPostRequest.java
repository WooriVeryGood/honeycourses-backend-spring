package org.wooriverygood.api.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostRequest {

    @NotBlank(message = "제목을 비우면 안됩니다.")
    private final String post_title;
    private final String post_category;
    private final String post_content;

    @Builder
    public NewPostRequest(String post_title, String post_category, String post_content) {
        this.post_title = post_title;
        this.post_category = post_category;
        this.post_content = post_content;
    }
}
