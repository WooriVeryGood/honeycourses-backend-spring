package org.wooriverygood.api.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.wooriverygood.api.post.exception.InvalidPostContentException;
import org.wooriverygood.api.post.exception.InvalidPostTitleException;

@Getter
@Embeddable
public class Title {

    private static final int MAX_TITLE_LENGTH = 45;

    @Column(name = "post_title", length = MAX_TITLE_LENGTH, nullable = false)
    private String value;


    protected Title() {
    }

    public Title(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidPostTitleException();
        }
        if (value.length() > MAX_TITLE_LENGTH) {
            throw new InvalidPostContentException();
        }
    }

}
