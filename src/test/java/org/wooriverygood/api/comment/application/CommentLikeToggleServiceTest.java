package org.wooriverygood.api.comment.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommentLikeToggleServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentLikeToggleService commentLikeToggleService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    private final int COMMENT_COUNT = 10;

    List<Comment> comments = new ArrayList<>();

    private Comment comment = Comment.builder()
            .id(2L)
            .post(post)
            .content("comment content")
            .author(authInfo.getUsername())
            .commentLikes(new ArrayList<>())
            .build();

    private Comment reply = Comment.builder()
            .id(3L)
            .post(post)
            .content("reply content")
            .author(authInfo.getUsername())
            .commentLikes(new ArrayList<>())
            .parent(comment)
            .build();


    @BeforeEach
    void setUpPosts() {
        comment.getReplies().add(reply);

        for (int i = 0; i < COMMENT_COUNT; i++) {
            Comment comment = Comment.builder()
                    .id((long) i)
                    .content("comment" + i)
                    .author("author" + i)
                    .post(post)
                    .build();
            comments.add(comment);
        }
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 올린다.")
    void likeComment_up() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        CommentLikeResponse response = commentLikeToggleService.likeComment(comment.getId(), authInfo);

        Assertions.assertThat(response.getLikeCount()).isEqualTo(comment.getLikeCount() + 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 내린다.")
    void likeComment_down() {
        CommentLike commentLike = CommentLike.builder()
                .id(2L)
                .comment(comment)
                .username(authInfo.getUsername())
                .build();
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByCommentAndUsername(any(Comment.class), anyString()))
                .thenReturn(Optional.ofNullable(commentLike));

        CommentLikeResponse response = commentLikeToggleService.likeComment(comment.getId(), authInfo);

        Assertions.assertThat(response.getLikeCount()).isEqualTo(comment.getLikeCount() - 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(false);
    }

}