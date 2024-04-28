package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostLike;
import org.wooriverygood.api.post.dto.PostLikeResponse;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PostLikeToggleServiceTest extends PostServiceTest {

    @InjectMocks
    private PostLikeToggleService postLikeToggleService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("특정 게시글에 좋아요를 1 올린다.")
    void likePost_up() {
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.findByPostAndMember(any(Post.class), any(Member.class)))
                .thenReturn(Optional.empty());

        PostLikeResponse response = postLikeToggleService.togglePostLike(post.getId(), authInfo);

        assertAll(
                () -> assertThat(response.getLikeCount()).isEqualTo(post.getLikeCount() + 1),
                () -> assertThat(response.isLiked()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 1 내린다.")
    void likePost_down() {
        PostLike postLike = PostLike.builder()
                .id(1L)
                .post(post)
                .username(authInfo.getUsername())
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.findByPostAndMember(any(Post.class), any(Member.class)))
                .thenReturn(Optional.ofNullable(postLike));

        PostLikeResponse response = postLikeToggleService.togglePostLike(postLike.getId(), authInfo);

        assertAll(
                () -> assertThat(response.getLikeCount()).isEqualTo(post.getLikeCount() - 1),
                () -> assertThat(response.isLiked()).isEqualTo(false)
        );
    }

}