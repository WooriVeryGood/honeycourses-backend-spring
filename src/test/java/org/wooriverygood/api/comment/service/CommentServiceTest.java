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
import org.wooriverygood.api.comment.dto.NewCommentRequest;
import org.wooriverygood.api.comment.dto.NewCommentResponse;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PostRepository postRepository;

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

    AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
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

    @Test
    @DisplayName("특정 게시글의 댓글을 작성한다.")
    void addComment() {
        NewCommentRequest newCommentRequest = NewCommentRequest.builder()
                .content("comment content")
                .build();

        Mockito.when(commentRepository.save(any(Comment.class)))
                .thenReturn(Comment.builder()
                        .author(authInfo.getUsername())
                        .content(newCommentRequest.getContent())
                        .build());

        Mockito.when(postRepository.findById(any()))
                .thenReturn(Optional.ofNullable(singlePost));

         NewCommentResponse response = commentService.addComment(authInfo, 1L, newCommentRequest);

         Assertions.assertThat(response.getAuthor()).isEqualTo(authInfo.getUsername());
         Assertions.assertThat(response.getContent()).isEqualTo(newCommentRequest.getContent());
    }

}