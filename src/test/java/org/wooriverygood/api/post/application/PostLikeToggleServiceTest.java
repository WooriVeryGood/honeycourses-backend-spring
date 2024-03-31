package org.wooriverygood.api.post.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.domain.PostLike;
import org.wooriverygood.api.post.dto.PostLikeResponse;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class PostLikeToggleServiceTest extends MockTest {

    @InjectMocks
    private PostLikeToggleService postLikeToggleService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("")
            .username("22222-34534-123")
            .build();

    private Post post = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author(authInfo.getUsername())
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    @Test
    @DisplayName("특정 게시글에 좋아요를 1 올린다.")
    void likePost_up() {
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));

        PostLikeResponse response = postLikeToggleService.togglePostLike(post.getId(), authInfo);

        Assertions.assertThat(response.getLikeCount()).isEqualTo(post.getLikeCount() + 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 1 내린다.")
    void likePost_down() {
        PostLike postLike = PostLike.builder()
                .id(1L)
                .post(post)
                .username(authInfo.getUsername())
                .build();
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));
        when(postLikeRepository.findByPostAndUsername(any(Post.class), anyString()))
                .thenReturn(Optional.ofNullable(postLike));

        PostLikeResponse response = postLikeToggleService.togglePostLike(postLike.getId(), authInfo);

        Assertions.assertThat(response.getLikeCount()).isEqualTo(post.getLikeCount() - 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(false);
    }

}