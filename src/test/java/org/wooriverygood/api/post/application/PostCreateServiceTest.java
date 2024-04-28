package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.exception.InvalidPostCategoryException;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostCreateServiceTest extends MockTest {

    @InjectMocks
    private PostCreateService postCreateService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("새로운 게시글을 작성한다.")
    void addPost() {
        NewPostRequest request = NewPostRequest.builder()
                .postTitle("title")
                .postCategory("자유")
                .postContent("content")
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

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

        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        assertThatThrownBy(() -> postCreateService.addPost(authInfo, newPostRequest))
                .isInstanceOf(InvalidPostCategoryException.class);
    }

}