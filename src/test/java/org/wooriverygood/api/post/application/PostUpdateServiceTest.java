package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.PostUpdateRequest;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class PostUpdateServiceTest extends PostServiceTest {

    @InjectMocks
    private PostUpdateService postUpdateService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    private Post noAuthPost = Post.builder()
            .id(99L)
            .category(PostCategory.OFFER)
            .title("title99")
            .content("content99")
            .member(new Member(5L, "username"))
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
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));

        postUpdateService.updatePost(post.getId(), request, authInfo);

        assertAll(
                () -> assertThat(post.getTitle()).isEqualTo(request.getPostTitle()),
                () -> assertThat(post.getContent()).isEqualTo(request.getPostContent())
        );
    }

    @Test
    @DisplayName("권한이 없는 게시글을 수정할 수 없다.")
    void updatePost_exception_noAuth() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .postTitle("new title")
                .postContent("new content")
                .build();
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(noAuthPost));

        assertThatThrownBy(() -> postUpdateService.updatePost(noAuthPost.getId(), request, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}