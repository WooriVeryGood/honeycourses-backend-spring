package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.BeforeEach;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;

public class PostServiceTest extends MockTest {

    protected Post post;

    @BeforeEach
    void dateSetUp() {
        post = Post.builder()
                .id(1L)
                .category(PostCategory.OFFER)
                .title("simple title")
                .content("simple content")
                .member(member)
                .comments(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .reports(new ArrayList<>())
                .build();
    }

}
