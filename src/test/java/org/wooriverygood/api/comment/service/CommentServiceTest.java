package org.wooriverygood.api.comment.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentResponse;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    private final int COMMENT_COUNT = 10;

    List<Comment> comments = new ArrayList<>();

    Post singlePost = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author("author6")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    @BeforeEach
    void setUpPosts() {
        for (int i = 0; i < COMMENT_COUNT; i++) {
            Comment comment = Comment.builder()
                    .id((long) i)
                    .content("comment" + i)
                    .author("author" + i)
                    .post(singlePost)
                    .build();
            comments.add(comment);
        }
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 게시글의 댓글들을 불러온다.")
    void findAllCommentsByPostId() {
        Mockito.when(commentRepository.findAllByPostId(any()))
                .thenReturn(comments);

        List<CommentResponse> responses = commentService.findAllCommentsByPostId(2L);

        Assertions.assertThat(responses.size()).isEqualTo(COMMENT_COUNT);
    }

}