package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewPostRequest {

    @NotBlank(message = "제목을 비우면 안됩니다.")
    private final String postTitle;

    private final String postCategory;

    private final String postContent;


    @Builder
    public NewPostRequest(String postTitle, String postCategory, String postContent) {
        this.postTitle = postTitle;
        this.postCategory = postCategory;
        this.postContent = postContent;
    }

}
