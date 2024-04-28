package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentsResponse;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommentFindServiceTest extends MockTest {

    @InjectMocks
    private CommentFindService commentFindService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    private final int COMMENT_COUNT = 10;

    private List<Comment> comments = new ArrayList<>();

    private Post post = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author("author6")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    @BeforeEach
    void setUp() {
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
    @DisplayName("유효한 id를 통해 특정 게시글의 댓글들을 불러온다.")
    void findAllCommentsByPostId() {
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);
        when(commentLikeRepository.existsByCommentAndUsername(any(Comment.class), anyString()))
                .thenReturn(true);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().size()).isEqualTo(COMMENT_COUNT);
    }

    @Test
    @DisplayName("삭제 처리될 댓글의 내용은 비어있다.")
    void findAllCommentsByPostId_softRemoved() {
        comments = new ArrayList<>();
        comments.add(Comment.builder()
                .id(1L)
                .content("Content")
                .author("Author")
                .post(post)
                .softRemoved(true)
                .build());
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().get(0).getCommentContent()).isEqualTo(null);
    }

    @Test
    @DisplayName("대댓글만 불러올 수 없다.")
    void findAllCommentsByPostId_cannot_only_reply() {
        Comment comment = Comment.builder()
                .id(1L)
                .content("Parent")
                .author("author1")
                .post(post)
                .build();
        comments = new ArrayList<>();
        comments.add(Comment.builder()
                .id(2L)
                .content("Child")
                .author("Author2")
                .post(post)
                .parent(comment)
                .build());
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글과 대댓글들을 모두 불러온다.")
    void findAllCommentsByPostId_withReplies() {
        comments = new ArrayList<>();
        Comment comment = Comment.builder()
                .id(1L)
                .content("Parent")
                .author("author1")
                .post(post)
                .build();
        comments.add(comment);
        Comment reply = Comment.builder()
                .id(2L)
                .content("Child")
                .author("Author2")
                .post(post)
                .parent(comment)
                .build();
        comments.add(reply);
        comment.addReply(reply);
        when(commentRepository.findAllByPostId(anyLong()))
                .thenReturn(comments);

        CommentsResponse response = commentFindService.findAllCommentsByPostId(2L, authInfo);

        assertThat(response.getComments().get(0).getReplies().size()).isEqualTo(1);
    }

}