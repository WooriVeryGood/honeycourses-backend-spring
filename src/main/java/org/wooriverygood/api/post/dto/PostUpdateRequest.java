package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostUpdateRequest {

    @NotBlank(message = "제목이 없습니다.")
    private String postTitle;

    private String postContent;


    @Builder
    public PostUpdateRequest(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

}
