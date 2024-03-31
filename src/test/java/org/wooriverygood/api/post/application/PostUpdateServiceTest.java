package org.wooriverygood.api.post.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.PostUpdateRequest;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PostUpdateServiceTest extends MockTest {

    @InjectMocks
    private PostUpdateService postUpdateService;

    @Mock
    private PostRepository postRepository;

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("")
            .username("22222-34534-123")
            .build();

    @Mock
    private Post post = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author(authInfo.getUsername())
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    private Post noAuthPost = Post.builder()
            .id(99L)
            .category(PostCategory.OFFER)
            .title("title99")
            .content("content99")
            .author("43434-45654-234")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    @Test
    @DisplayName("권한이 있는 게시글을 수정한다.")
    void updatePost() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .postTitle("new title")
                .postContent("new content")
                .build();

        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(post));

        postUpdateService.updatePost(post.getId(), request, authInfo);

    }

    @Test
    @DisplayName("권한이 없는 게시글을 수정할 수 없다.")
    void updatePost_exception_noAuth() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .postTitle("new title")
                .postContent("new content")
                .build();

        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(noAuthPost));

        Assertions.assertThatThrownBy(() -> postUpdateService.updatePost(noAuthPost.getId(), request, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}