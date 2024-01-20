package org.wooriverygood.api.post.domain;

import lombok.Getter;
import org.wooriverygood.api.advice.exception.InvalidPostCategoryException;

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

    public static PostCategory parse(String value) throws InvalidPostCategoryException {
        return switch (value) {
            case "자유" -> PostCategory.FREE;
            case "질문" -> PostCategory.QUESTION;
            case "중고거래" -> PostCategory.TRADE;
            case "구인" -> PostCategory.OFFER;
            default -> throw new InvalidPostCategoryException();
        };
    }
}
