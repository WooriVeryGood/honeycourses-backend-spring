package org.wooriverygood.api.comment.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CommentLikeToggleServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentLikeToggleService commentLikeToggleService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("특정 댓글의 좋아요를 1 올린다.")
    void likeComment_up() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByCommentAndMember(any(Comment.class), any(Member.class)))
                .thenReturn(Optional.empty());

        CommentLikeResponse response = commentLikeToggleService.likeComment(comment.getId(), authInfo);

        assertAll(
                () -> assertThat(response.getLikeCount()).isEqualTo(comment.getLikeCount() + 1),
                () -> assertThat(response.isLiked()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("특정 댓글의 좋아요를 1 내린다.")
    void likeComment_down() {
        CommentLike commentLike = CommentLike.builder()
                .id(1L)
                .comment(comment)
                .member(member)
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));
        when(commentLikeRepository.findByCommentAndMember(any(Comment.class), any(Member.class)))
                .thenReturn(Optional.ofNullable(commentLike));

        CommentLikeResponse response = commentLikeToggleService.likeComment(comment.getId(), authInfo);

        assertAll(
                () -> assertThat(response.getLikeCount()).isEqualTo(comment.getLikeCount() - 1),
                () -> assertThat(response.isLiked()).isEqualTo(false)
        );
    }

}