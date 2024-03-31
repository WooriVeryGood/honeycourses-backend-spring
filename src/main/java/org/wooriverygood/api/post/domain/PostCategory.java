package org.wooriverygood.api.post.domain;

import lombok.Getter;
import org.wooriverygood.api.post.exception.InvalidPostCategoryException;

@Getter
public enum PostCategory {

    FREE("자유"), QUESTION("질문"), TRADE("중고거래"), OFFER("구인"), NOTICE("공지");

    private final String value;

    PostCategory(String value) {
        this.value = value;
    }

    public boolean equalsTo(String value) {
        return this.value.equals(value);
    }

    public static PostCategory parse(String value)  {
        return switch (value) {
            case "자유", "free" -> PostCategory.FREE;
            case "질문", "question" -> PostCategory.QUESTION;
            case "중고거래", "trade" -> PostCategory.TRADE;
            case "구인", "offer" -> PostCategory.OFFER;
            case "공지", "notice" -> PostCategory.NOTICE;
            default -> throw new InvalidPostCategoryException();
        };
    }
}
