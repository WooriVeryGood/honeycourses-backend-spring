package org.wooriverygood.api.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.post.domain.PostCategory;

@Getter
public class NewPostResponse {

    private final Long post_id;
    private final String title;
    private final String category;
    private final String author;

    @Builder
    public NewPostResponse(Long post_id, String title, String category, String author) {
        this.post_id = post_id;
        this.title = title;
        this.category = category;
        this.author = author;
    }
}
