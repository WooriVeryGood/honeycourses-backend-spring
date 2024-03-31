package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class PostDeleteServiceTest extends MockTest {

    @InjectMocks
    private PostDeleteService postDeleteService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    private Post singlePost;

    private AuthInfo authInfo;

    @BeforeEach
    void setUp() {
        authInfo = AuthInfo.builder()
                .sub("22222-34534-123")
                .username("22222-34534-123")
                .build();

        singlePost = Post.builder()
                .id(6L)
                .category(PostCategory.OFFER)
                .title("title6")
                .content("content6")
                .author(authInfo.getUsername())
                .comments(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("권한이 있는 게시글을 삭제한다.")
    void deletePost() {
        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singlePost));

        postDeleteService.deletePost(authInfo, singlePost.getId());

        verify(commentRepository).deleteAllByPost(singlePost);
        verify(postLikeRepository).deleteAllByPost(singlePost);
    }

    @Test
    @DisplayName("권한이 없는 게시글을 삭제한다.")
    void deletePost_exception_noAuth() {
        Post noAuthPost = Post.builder()
                .id(99L)
                .category(PostCategory.OFFER)
                .title("title99")
                .content("content99")
                .author("43434-45654-234")
                .comments(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .build();

        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(noAuthPost));

        assertThatThrownBy(() -> postDeleteService.deletePost(authInfo, singlePost.getId()))
                .isInstanceOf(AuthorizationException.class);
    }

}