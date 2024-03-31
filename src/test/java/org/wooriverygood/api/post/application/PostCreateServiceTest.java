package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.exception.InvalidPostCategoryException;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class PostCreateServiceTest extends MockTest {

    @InjectMocks
    private PostCreateService postCreateService;

    @Mock
    private PostRepository postRepository;

    private AuthInfo authInfo;

    @BeforeEach
    void setUp() {
        authInfo = AuthInfo.builder()
                .sub("22222-34534-123")
                .username("22222-34534-123")
                .build();
    }

    @Test
    @DisplayName("새로운 게시글을 작성한다.")
    void addPost() {
        NewPostRequest request = NewPostRequest.builder()
                .postTitle("title")
                .postCategory("자유")
                .postContent("content")
                .build();

        postCreateService.addPost(authInfo, request);

        verify(postRepository).save(any(Post.class));
    }

    @Test
    @DisplayName("새로운 게시글의 카테고리가 유효하지 않으면 등록에 실패한다.")
    void addPost_exception_invalid_category() {
        NewPostRequest newPostRequest = NewPostRequest.builder()
                .postTitle("title")
                .postCategory("자유유")
                .postContent("content")
                .build();

        assertThatThrownBy(() -> postCreateService.addPost(authInfo, newPostRequest))
                .isInstanceOf(InvalidPostCategoryException.class);
    }

}