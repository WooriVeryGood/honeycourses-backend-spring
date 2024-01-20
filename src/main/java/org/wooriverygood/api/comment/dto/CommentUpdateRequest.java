package org.wooriverygood.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequest {

    @NotBlank(message = "댓글의 내용이 없습니다.")
    private String content;

    @Builder
    public CommentUpdateRequest(String content) {
        this.content = content;
    }
}
