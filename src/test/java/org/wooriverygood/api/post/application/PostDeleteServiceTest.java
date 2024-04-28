package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostDeleteServiceTest extends PostServiceTest {

    @InjectMocks
    private PostDeleteService postDeleteService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("권한이 있는 게시글을 삭제한다.")
    void deletePost() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));

        postDeleteService.deletePost(authInfo, post.getId());

        assertAll(
                () -> verify(commentRepository).deleteAllByPost(post),
                () -> verify(postLikeRepository).deleteAllByPost(post)
        );
    }

    @Test
    @DisplayName("권한이 없는 게시글을 삭제하면 예외가 발생한다.")
    void deletePost_exception_noAuth() {
        Post noAuthPost = Post.builder()
                .id(9L)
                .title("title")
                .content("content")
                .member(new Member(5L, "username"))
                .build();

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(noAuthPost));

        assertThatThrownBy(() -> postDeleteService.deletePost(authInfo, post.getId()))
                .isInstanceOf(AuthorizationException.class);
    }

}