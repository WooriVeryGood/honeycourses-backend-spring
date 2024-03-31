package org.wooriverygood.api.post.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.PostDetailResponse;
import org.wooriverygood.api.post.dto.PostsResponse;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class PostFindServiceTest extends MockTest {

    @InjectMocks
    private PostFindService postFindService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    private List<Post> posts = new ArrayList<>();

    private List<Post> myPosts = new ArrayList<>();

    private List<Post> freePosts = new ArrayList<>();

    private AuthInfo authInfo;

    private final int POST_COUNT = 23;

    @BeforeEach
    void setUp() {
        authInfo = AuthInfo.builder()
                .sub("22222-34534-123")
                .username("22222-34534-123")
                .build();

        for (long i = 1; i <= POST_COUNT; i++) {
            Post post = Post.builder()
                    .id(i)
                    .category(PostCategory.FREE)
                    .title("title" + i)
                    .content("content" + i)
                    .author(authInfo.getUsername())
                    .comments(new ArrayList<>())
                    .postLikes(new ArrayList<>())
                    .build();
            posts.add(post);
            if (i > 9) {
                myPosts.add(post);
                freePosts.add(post);
            }
        }
    }

    @Test
    @DisplayName("로그인 한 상황에서 게시글을 불러온다.")
    void findPosts_login() {
        Pageable pageable = PageRequest.of(0, 10);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), this.posts.size());
        List<Post> posts = this.posts.subList(start, end);
        PageImpl<Post> page = new PageImpl<>(posts, pageable, this.posts.size());
        when(postRepository.findAllByOrderByIdDesc(any(PageRequest.class)))
                .thenReturn(page);

        PostsResponse response = postFindService.findPosts(authInfo, pageable, "");

        assertThat(response.getPosts().size()).isEqualTo(10);
        assertThat(response.getTotalPageCount()).isEqualTo(3);
        assertThat(response.getTotalPostCount()).isEqualTo(POST_COUNT);
    }

    @Test
    @DisplayName("자유 카테고리의 게시글을 불러온다.")
    void findPosts_category_free() {
        Pageable pageable = PageRequest.of(0, 10);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), this.freePosts.size());
        List<Post> posts = this.freePosts.subList(start, end);
        PageImpl<Post> page = new PageImpl<>(posts, pageable, this.freePosts.size());
        when(postRepository.findAllByCategoryOrderByIdDesc(any(PostCategory.class), any(PageRequest.class)))
                .thenReturn(page);

        PostsResponse response = postFindService.findPosts(authInfo, pageable, "자유");

        assertThat(response.getPosts().size()).isEqualTo(10);
        assertThat(response.getTotalPageCount()).isEqualTo(2);
        assertThat(response.getTotalPostCount()).isEqualTo(14);
    }

    @Test
    @DisplayName("유효한 id를 이용하여 특정 게시글을 불러온다.")
    void findPostById() {
        Post singlePost = Post.builder()
                .id(6L)
                .category(PostCategory.OFFER)
                .title("title6")
                .content("content6")
                .author(authInfo.getUsername())
                .comments(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .build();
        when(postRepository.findById(any()))
                .thenReturn(Optional.ofNullable(singlePost));

        PostDetailResponse response = postFindService.findPostById(6L, authInfo);

        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 id를 이용하여 특정 게시글을 불러오면 에러를 반환한다.")
    void findPostById_exception_invalidId() {
        when(postRepository.findById(any()))
                .thenThrow(PostNotFoundException.class);

        Assertions.assertThatThrownBy(() -> postFindService.findPostById(6L, authInfo))
                .isInstanceOf(PostNotFoundException.class);
    }


    @Test
    @DisplayName("사용자 본인이 작성한 게시글을 불러온다.")
    void findMyPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), this.myPosts.size());
        List<Post> posts = this.myPosts.subList(start, end);
        PageImpl<Post> page = new PageImpl<>(posts, pageable, this.myPosts.size());
        when(postRepository.findByAuthorOrderByIdDesc(any(String.class), any(PageRequest.class)))
                .thenReturn(page);

        PostsResponse response = postFindService.findMyPosts(authInfo, pageable);

        assertThat(response.getPosts().size()).isEqualTo(10);
        assertThat(response.getTotalPageCount()).isEqualTo(2);
        assertThat(response.getTotalPostCount()).isEqualTo(14);
    }

}