package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.dto.CommentUpdateRequest;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CommentUpdateServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentUpdateService commentUpdateService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("권한이 있는 댓글을 수정한다.")
    void updateComment() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("new comment content")
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentUpdateService.updateComment(comment.getId(), request, authInfo);

        assertAll(
                () -> assertThat(comment.isUpdated()).isEqualTo(true),
                () -> assertThat(comment.getContent()).isEqualTo(request.getContent())
        );
    }

    @Test
    @DisplayName("권한이 없는 댓글을 수정할 수 없다.")
    void updateComment_exception_noAuth() {
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                .content("new comment content")
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(new Member(5L, "username")));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        assertThatThrownBy(() -> commentUpdateService.updateComment(comment.getId(), request, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}