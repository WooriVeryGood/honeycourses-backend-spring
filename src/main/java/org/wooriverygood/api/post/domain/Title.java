package org.wooriverygood.api.post.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.post.exception.InvalidPostContentException;
import org.wooriverygood.api.post.exception.InvalidPostTitleException;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Title {

    private static final int MAX_TITLE_LENGTH = 45;

    @Column(name = "post_title", length = MAX_TITLE_LENGTH, nullable = false)
    private String value;


    public Title(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank())
            throw new InvalidPostTitleException();

        if (value.length() > MAX_TITLE_LENGTH)
            throw new InvalidPostContentException();
    }

}
