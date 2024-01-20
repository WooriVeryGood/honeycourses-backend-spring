package org.wooriverygood.api.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

    @NotBlank(message = "제목이 없습니다.")
    private String post_title;

    private String post_content;

    @Builder
    public PostUpdateRequest(String post_title, String post_content) {
        this.post_title = post_title;
        this.post_content = post_content;
    }
}
