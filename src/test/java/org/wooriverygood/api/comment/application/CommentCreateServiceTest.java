package org.wooriverygood.api.comment.application;

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
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentCreateServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentCreateService commentCreateService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("특정 게시글의 댓글을 작성한다.")
    void addComment() {
        NewCommentRequest request = NewCommentRequest.builder()
                .content("comment content")
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));

        commentCreateService.addComment(authInfo, post.getId(), request);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("특정 댓글의 대댓글을 작성한다.")
    void addReply() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(commentWithoutReply));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        commentCreateService.addReply(commentWithoutReply.getId(), request, authInfo);

        assertAll(
                () -> assertThat(commentWithoutReply.getReplies().size()).isEqualTo(1),
                () -> assertThat(commentWithoutReply.getReplies().get(0).getContent()).isEqualTo(request.getContent()),
                () -> assertThat(commentWithoutReply.getReplies().get(0).getParent()).isEqualTo(commentWithoutReply)
        );
    }

    @Test
    @DisplayName("대댓글의 대댓글을 작성할 수 없다.")
    void addReply_exception_depth() {
        NewReplyRequest request = NewReplyRequest.builder()
                .content("reply content")
                .build();
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));

        assertThatThrownBy(() -> commentCreateService.addReply(reply.getId(), request, authInfo))
                .isInstanceOf(ReplyDepthException.class);
    }

}