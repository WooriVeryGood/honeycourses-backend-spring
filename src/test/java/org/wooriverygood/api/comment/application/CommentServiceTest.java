package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.BeforeEach;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;

public class CommentServiceTest extends MockTest {

    protected Post post;

    protected Comment comment;

    protected Comment commentWithoutReply;

    protected Comment reply;


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
                .build();
        comment = Comment.builder()
                .id(1L)
                .content("content")
                .post(post)
                .member(member)
                .commentLikes(new ArrayList<>())
                .reports(new ArrayList<>())
                .build();
        reply = Comment.builder()
                .id(2L)
                .content("content")
                .post(post)
                .parent(comment)
                .member(member)
                .build();
        comment.addReply(reply);
        commentWithoutReply = Comment.builder()
                .id(3L)
                .content("content")
                .post(post)
                .member(member)
                .build();
    }

}
