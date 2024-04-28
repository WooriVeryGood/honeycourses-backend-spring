package org.wooriverygood.api.post.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.PostDetailResponse;
import org.wooriverygood.api.post.dto.PostsResponse;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class PostFindServiceTest extends PostServiceTest {

    @InjectMocks
    private PostFindService postFindService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private MemberRepository memberRepository;

    private List<Post> posts = new ArrayList<>();

    private List<Post> myPosts = new ArrayList<>();

    private List<Post> freePosts = new ArrayList<>();

    private final int POST_COUNT = 23;


    @BeforeEach
    void setUp() {
        posts.clear();
        myPosts.clear();
        freePosts.clear();
        for (long i = 1; i <= POST_COUNT; i++) {
            Post post = Post.builder()
                    .id(i)
                    .category(PostCategory.FREE)
                    .title("title" + i)
                    .content("content" + i)
                    .member(member)
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
    @DisplayName("로그인 한 상황에서 모든 카테고리의 게시글을 불러온다.")
    void findPosts_login() {
        Pageable pageable = PageRequest.of(0, 10);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), this.posts.size());
        List<Post> posts = this.posts.subList(start, end);
        PageImpl<Post> page = new PageImpl<>(posts, pageable, this.posts.size());
        when(postRepository.findAllByOrderByIdDesc(any(PageRequest.class)))
                .thenReturn(page);
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postLikeRepository.existsByPostAndMember(any(Post.class), any(Member.class)))
                .thenReturn(true);

        PostsResponse response = postFindService.findPosts(authInfo, pageable, "");

        assertAll(
                () -> assertThat(response.getPosts().size()).isEqualTo(10),
                () -> assertThat(response.getTotalPageCount()).isEqualTo(3),
                () -> assertThat(response.getTotalPostCount()).isEqualTo(POST_COUNT)
        );
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
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        PostsResponse response = postFindService.findPosts(authInfo, pageable, "자유");

        assertAll(
                () -> assertThat(response.getPosts().size()).isEqualTo(10),
                () -> assertThat(response.getTotalPageCount()).isEqualTo(2),
                () -> assertThat(response.getTotalPostCount()).isEqualTo(14)
        );
    }

    @Test
    @DisplayName("유효한 id를 이용하여 특정 게시글을 불러온다.")
    void findPostById() {
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        PostDetailResponse response = postFindService.findPostById(6L, authInfo);

        assertThat(response.getPostId()).isEqualTo(post.getId());
    }

    @Test
    @DisplayName("유효하지 않은 id를 이용하여 특정 게시글을 불러오면 에러를 반환한다.")
    void findPostById_exception_invalidId() {
        when(postRepository.findById(any()))
                .thenThrow(PostNotFoundException.class);

        assertThatThrownBy(() -> postFindService.findPostById(6L, authInfo))
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
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));

        PostsResponse response = postFindService.findMyPosts(authInfo, pageable);

        assertAll(
                () -> assertThat(response.getPosts().size()).isEqualTo(10),
                () -> assertThat(response.getTotalPageCount()).isEqualTo(2),
                () -> assertThat(response.getTotalPostCount()).isEqualTo(14)
        );
    }

}