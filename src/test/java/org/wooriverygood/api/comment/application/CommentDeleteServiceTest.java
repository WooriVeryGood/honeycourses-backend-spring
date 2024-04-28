package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CommentDeleteServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentDeleteService commentDeleteService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("권한이 있는 댓글을 삭제한다.")
    void deleteComment() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        comment.deleteReply(reply);

        commentDeleteService.deleteComment(comment.getId(), authInfo);

        assertAll(
                () -> verify(commentLikeRepository).deleteAllByComment(comment),
                () -> verify(commentRepository).delete(comment)
        );
    }

    @Test
    @DisplayName("권한이 있는 대댓글을 삭제한다.")
    void deleteReply() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));

        commentDeleteService.deleteComment(reply.getId(), authInfo);

        assertAll(
                () -> verify(commentLikeRepository).deleteAllByComment(reply),
                () -> verify(commentRepository).delete(reply)
        );
    }

    @Test
    @DisplayName("권한이 없는 대댓글을 삭제할 수 없다.")
    void deleteReply_exception_noAuth() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Member(5L, "username")));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));

        assertThatThrownBy(() -> commentDeleteService.deleteComment(reply.getId(), authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("부모 댓글을 삭제해도 대댓글은 남아있다.")
    void deleteComment_keepChildren() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentDeleteService.deleteComment(comment.getId(), authInfo);

        assertAll(
                () -> assertThat(comment.isSoftRemoved()).isEqualTo(true),
                () -> assertThat(comment.canDelete()).isEqualTo(false)
        );
    }

    @Test
    @DisplayName("특정 대댓글 삭제 후, 삭제 예정으로 처리되고 대댓글이 없는 부모 댓글을 삭제한다.")
    void deletePrentAndReply() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));
        comment.willBeDeleted();

        commentDeleteService.deleteComment(reply.getId(), authInfo);

        assertAll(
                () -> verify(commentRepository).delete(reply),
                () -> verify(commentRepository).delete(comment)
        );
    }

    @Test
    @DisplayName("권한이 없는 댓글은 삭제할 수 없다")
    void deleteComment_exception_noAuth() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Member(5L, "username")));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        assertThatThrownBy(() -> commentDeleteService.deleteComment(comment.getId(), authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}