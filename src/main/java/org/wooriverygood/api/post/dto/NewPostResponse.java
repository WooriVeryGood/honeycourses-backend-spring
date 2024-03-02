package org.wooriverygood.api.post.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewPostResponse {

    private final Long postId;
    private final String title;
    private final String category;
    private final String author;

    @Builder
    public NewPostResponse(Long postId, String title, String category, String author) {
        this.postId = postId;
        this.title = title;
        this.category = category;
        this.author = author;
    }
}
