package org.wooriverygood.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewReplyRequest {

    @NotBlank(message = "댓글의 내용을 비우면 안됩니다.")
    private String content;


    @Builder
    public NewReplyRequest(String content) {
        this.content = content;
    }
}
