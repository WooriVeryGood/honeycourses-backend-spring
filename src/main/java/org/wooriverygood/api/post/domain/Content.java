package org.wooriverygood.api.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.wooriverygood.api.post.exception.InvalidPostContentException;

@Embeddable
@Getter
public class Content {

    private static final int MAX_CONTENT_LENGTH = 2000;

    @Column(name = "post_content", length = MAX_CONTENT_LENGTH)
    private String value;


    protected Content() {
    }

    public Content(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value.length() > MAX_CONTENT_LENGTH) {
            throw new InvalidPostContentException();
        }
    }

}
