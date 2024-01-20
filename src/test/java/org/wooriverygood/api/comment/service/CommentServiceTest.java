package org.wooriverygood.api.comment.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

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

    Comment singleComment = Comment.builder()
            .id(2L)
            .post(singlePost)
            .content("comment content")
            .author(authInfo.getUsername())
            .commentLikes(new ArrayList<>())
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

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 올린다.")
    void likeComment_up() {
        Mockito.when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleComment));

        CommentLikeResponse response = commentService.likeComment(singleComment.getId(), authInfo);

        Assertions.assertThat(response.getLike_count()).isEqualTo(singleComment.getLikeCount() + 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 내린다.")
    void likeComment_down() {
        CommentLike commentLike = CommentLike.builder()
                .id(2L)
                .comment(singleComment)
                .username(authInfo.getUsername())
                .build();
        Mockito.when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleComment));
        Mockito.when(commentLikeRepository.findByCommentAndUsername(any(Comment.class), any(String.class)))
                .thenReturn(Optional.ofNullable(commentLike));

        CommentLikeResponse response = commentService.likeComment(singleComment.getId(), authInfo);

        Assertions.assertThat(response.getLike_count()).isEqualTo(singleComment.getLikeCount() - 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(false);
    }

    @Test
    @DisplayName("권한이 있는 댓글을 삭제한다.")
    void deleteComment() {
        Mockito.when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleComment));

        CommentDeleteResponse response = commentService.deleteComment(singleComment.getId(), authInfo);

        Assertions.assertThat(response.getComment_id()).isEqualTo(singleComment.getId());
    }

    @Test
    @DisplayName("권한이 없는 댓글은 삭제할 수 없다")
    void deleteComment_exception_noAuth() {
        Mockito.when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleComment));

        AuthInfo noAuthInfo = AuthInfo.builder()
                .sub("no")
                .username("no")
                .build();

        Assertions.assertThatThrownBy(() -> commentService.deleteComment(singleComment.getId(), noAuthInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}