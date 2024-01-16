package org.wooriverygood.api.post.domain;

import lombok.Getter;

@Getter
public enum PostCategory {
    FREE("자유"), QUESTION("질문"), TRADE("중고거래"), OFFER("구인");

    private final String value;

    PostCategory(String value) {
        this.value = value;
    }

    public boolean equalsTo(String value) {
        return this.value.equals(value);
    }
}
