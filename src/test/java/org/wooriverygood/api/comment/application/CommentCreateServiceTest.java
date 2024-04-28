package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.exception.ReplyDepthException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.NewCommentRequest;
import org.wooriverygood.api.comment.dto.NewReplyRequest;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentCreateServiceTest extends MockTest {

    @InjectMocks
    private CommentCreateService commentCreateService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    private Post post = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author("author6")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    private Comment comment;

    private Comment reply;


    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .post(post)
                .build();
        reply = Comment.builder()
                .parent(comment)
                .build();
    }

    @Test
    @DisplayName("특정 게시글의 댓글을 작성한다.")
    void addComment() {
        NewCommentRequest newCommentRequest = NewCommentRequest.builder()
                .content("comment content")
                .build();

        when(commentRepository.save(any(Comment.class)))
                .thenReturn(Comment.builder()
                        .author(authInfo.getUsername())
                        .content(newCommentRequest.getContent())
                        .build());

        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));

        commentCreateService.addComment(authInfo, 1L, newCommentRequest);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("특정 댓글의 대댓글을 작성한다.")
    void addReply() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentCreateService.addReply(1L, request, authInfo);
        Comment reply = comment.getReplies().get(0);

        assertThat(reply.getContent()).isEqualTo(request.getContent());
        assertThat(reply.getParent()).isEqualTo(comment);
    }

    @Test
    @DisplayName("대댓글의 대댓글을 작성할 수 없다.")
    void addReply_exception_depth() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));

        assertThatThrownBy(() -> commentCreateService.addReply(1L, request, authInfo))
                .isInstanceOf(ReplyDepthException.class);
    }

}