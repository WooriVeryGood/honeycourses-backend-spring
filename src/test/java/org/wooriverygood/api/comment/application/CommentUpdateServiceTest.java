package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentUpdateRequest;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CommentUpdateServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentUpdateService commentUpdateService;

    @Mock
    private CommentRepository commentRepository;

    private Comment comment;


    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .id(2L)
                .post(post)
                .content("comment content")
                .author(authInfo.getUsername())
                .commentLikes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("권한이 있는 댓글을 수정한다.")
    void updateComment() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("new comment content")
                .build();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentUpdateService.updateComment(comment.getId(), request, authInfo);

        assertThat(comment.isUpdated()).isEqualTo(true);
        assertThat(comment.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("권한이 없는 댓글을 수정할 수 없다.")
    void updateComment_exception_noAuth() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("new comment content")
                .build();
        AuthInfo noAuthInfo = AuthInfo.builder()
                .sub("no")
                .username("no")
                .build();

        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        assertThatThrownBy(() -> commentUpdateService.updateComment(comment.getId(), request, noAuthInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}