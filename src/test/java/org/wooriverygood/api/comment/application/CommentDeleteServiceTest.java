package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommentDeleteServiceTest extends MockTest {

    @InjectMocks
    private CommentDeleteService commentDeleteService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private Comment comment;

    @Mock
    private Comment reply;


    @Test
    @DisplayName("권한이 있는 댓글을 삭제한다.")
    void deleteComment() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        when(comment.isParent())
                .thenReturn(true);
        when(comment.hasNoReply())
                .thenReturn(true);

        commentDeleteService.deleteComment(comment.getId(), authInfo);

        verify(comment).validateAuthor(authInfo.getUsername());
        verify(commentLikeRepository).deleteAllByComment(comment);
        verify(commentRepository).delete(comment);
    }

    @Test
    @DisplayName("권한이 있는 대댓글을 삭제한다.")
    void deleteReply() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));
        when(reply.isParent())
                .thenReturn(false);
        when(reply.getParent())
                .thenReturn(comment);
        commentDeleteService.deleteComment(reply.getId(), authInfo);

        verify(reply).validateAuthor(authInfo.getUsername());
        verify(commentLikeRepository).deleteAllByComment(reply);
        verify(comment).deleteReply(reply);
        verify(commentRepository).delete(reply);
    }

    @Test
    @DisplayName("권한이 없는 대댓글을 삭제할 수 없다.")
    void deleteReply_exception_noAuth() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));
        doThrow(new AuthorizationException())
                .when(reply)
                .validateAuthor(anyString());

        assertThatThrownBy(() -> commentDeleteService.deleteComment(reply.getId(), authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("부모 댓글을 삭제해도 대댓글은 남아있다.")
    void deleteComment_keepChildren() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        when(comment.isParent())
                .thenReturn(true);
        when(comment.hasNoReply())
                .thenReturn(false);

        commentDeleteService.deleteComment(comment.getId(), authInfo);

        verify(comment).willBeDeleted();
    }

    @Test
    @DisplayName("특정 대댓글 삭제 후, 삭제 예정으로 처리되고 대댓글이 없는 부모 댓글을 삭제한다.")
    void deletePrentAndReply() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(reply));
        when(reply.isParent())
                .thenReturn(false);
        when(reply.getParent())
                .thenReturn(comment);
        when(comment.canDelete())
                .thenReturn(true);

        commentDeleteService.deleteComment(reply.getId(), authInfo);

        verify(comment).deleteReply(reply);
        verify(commentRepository).delete(reply);
        verify(commentRepository).delete(comment);
    }

    @Test
    @DisplayName("권한이 없는 댓글은 삭제할 수 없다")
    void deleteComment_exception_noAuth() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        doThrow(new AuthorizationException())
                .when(comment)
                .validateAuthor(anyString());

        assertThatThrownBy(() -> commentDeleteService.deleteComment(comment.getId(), authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}