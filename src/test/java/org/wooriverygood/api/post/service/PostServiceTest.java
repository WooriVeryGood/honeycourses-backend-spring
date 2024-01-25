package org.wooriverygood.api.post.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.advice.exception.InvalidPostCategoryException;
import org.wooriverygood.api.advice.exception.PostNotFoundException;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.domain.PostLike;
import org.wooriverygood.api.post.dto.*;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private CommentRepository commentRepository;

    private final int POST_COUNT = 23;

    List<Post> posts = new ArrayList<>();

    AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    Post singlePost = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author(authInfo.getUsername())
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    Post noAuthPost = Post.builder()
            .id(99L)
            .category(PostCategory.OFFER)
            .title("title99")
            .content("content99")
            .author("43434-45654-234")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();


    @BeforeEach
    void setUpPosts() {
        for (int i = 1; i <= POST_COUNT; i++) {
            Post post = Post.builder()
                    .id((long) i)
                    .category(PostCategory.FREE)
                    .title("title" + i)
                    .content("content" + i)
                    .author(authInfo.getUsername())
                    .comments(new ArrayList<>())
                    .postLikes(new ArrayList<>())
                    .build();
            posts.add(post);
        }
    }

    @Test
    @DisplayName("로그인 한 상황에서 게시글을 불러온다.")
    void findAllPosts_login() {
        Pageable pageable = PageRequest.of(0, 10);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), this.posts.size());
        List<Post> posts = this.posts.subList(start, end);
        PageImpl<Post> page = new PageImpl<>(posts, pageable, this.posts.size());
        Mockito.when(postRepository.findAllByOrderByIdDesc(any(PageRequest.class)))
                .thenReturn(page);

        PostsResponse response = postService.findPosts(authInfo, pageable);

        Assertions.assertThat(response.getPosts().size()).isEqualTo(10);
        Assertions.assertThat(response.getTotalPageCount()).isEqualTo(3);
        Assertions.assertThat(response.getTotalPostCount()).isEqualTo(POST_COUNT);
    }

    @Test
    @DisplayName("유효한 id를 이용하여 특정 게시글을 불러온다.")
    void findPostById() {
        Mockito.when(postRepository.findById(any()))
                .thenReturn(Optional.ofNullable(singlePost));

        PostResponse response = postService.findPostById(6L, authInfo);

        Assertions.assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 id를 이용하여 특정 게시글을 불러오면 에러를 반환한다.")
    void findPostById_exception_invalidId() {
        Mockito.when(postRepository.findById(any()))
                .thenThrow(PostNotFoundException.class);

        Assertions.assertThatThrownBy(() -> postService.findPostById(6L, authInfo))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("새로운 게시글을 작성한다.")
    void addPost() {
        NewPostRequest newPostRequest = NewPostRequest.builder()
                .post_title("title")
                .post_category("자유")
                .post_content("content")
                .build();

        Mockito.when(postRepository.save(any(Post.class)))
                .thenReturn(Post.builder()
                        .title(newPostRequest.getPost_title())
                        .category(PostCategory.parse(newPostRequest.getPost_category()))
                        .content(newPostRequest.getPost_content())
                        .author(authInfo.getUsername())
                        .build());

        NewPostResponse response = postService.addPost(authInfo, newPostRequest);

        Assertions.assertThat(response.getTitle()).isEqualTo(newPostRequest.getPost_title());
        Assertions.assertThat(response.getCategory()).isEqualTo(newPostRequest.getPost_category());
        Assertions.assertThat(response.getAuthor()).isEqualTo(authInfo.getUsername());
    }

    @Test
    @DisplayName("새로운 게시글의 카테고리가 유효하지 않으면 등록에 실패한다.")
    void addPost_exception_invalid_category() {
        NewPostRequest newPostRequest = NewPostRequest.builder()
                .post_title("title")
                .post_category("자유유")
                .post_content("content")
                .build();

        Assertions.assertThatThrownBy(() -> postService.addPost(authInfo, newPostRequest))
                        .isInstanceOf(InvalidPostCategoryException.class);
    }

    @Test
    @DisplayName("사용자 본인이 작성한 게시글을 불러온다.")
    void findMyPosts() {
        Mockito.when(postRepository.findByAuthor(any(String.class)))
                .thenReturn(posts);

        List<PostResponse> responses = postService.findMyPosts(authInfo);

        Assertions.assertThat(responses.get(0).getPost_author()).isEqualTo(authInfo.getUsername());
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 1 올린다.")
    void likePost_up() {
        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singlePost));

        PostLikeResponse response = postService.likePost(singlePost.getId(), authInfo);

        Assertions.assertThat(response.getLike_count()).isEqualTo(singlePost.getLikeCount() + 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 1 내린다.")
    void likePost_down() {
        PostLike postLike = PostLike.builder()
                .id(1L)
                .post(singlePost)
                .username(authInfo.getUsername())
                .build();
        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singlePost));
        Mockito.when(postLikeRepository.findByPostAndUsername(any(Post.class), any(String.class)))
                .thenReturn(Optional.ofNullable(postLike));

        PostLikeResponse response = postService.likePost(singlePost.getId(), authInfo);

        Assertions.assertThat(response.getLike_count()).isEqualTo(singlePost.getLikeCount() - 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(false);
    }

    @Test
    @DisplayName("권한이 있는 게시글을 수정한다.")
    void updatePost() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .post_title("new title")
                .post_content("new content")
                .build();

        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singlePost));

        PostUpdateResponse response = postService.updatePost(singlePost.getId(), request, authInfo);

        Assertions.assertThat(response.getPost_id()).isEqualTo(singlePost.getId());
        Assertions.assertThat(singlePost.isUpdated()).isEqualTo(true);
    }

    @Test
    @DisplayName("권한이 없는 게시글을 수정할 수 없다.")
    void updatePost_exception_noAuth() {
        PostUpdateRequest request = PostUpdateRequest.builder()
                .post_title("new title")
                .post_content("new content")
                .build();

        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(noAuthPost));

        Assertions.assertThatThrownBy(() -> postService.updatePost(noAuthPost.getId(), request, authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("권한이 있는 게시글을 삭제한다.")
    void deletePost() {
        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singlePost));

        PostDeleteResponse response = postService.deletePost(singlePost.getId(), authInfo);

        Assertions.assertThat(response.getPost_id()).isEqualTo(singlePost.getId());
    }

    @Test
    @DisplayName("권한이 없는 게시글을 삭제한다.")
    void deletePost_exception_noAuth() {
        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(noAuthPost));

        Assertions.assertThatThrownBy(() -> postService.deletePost(noAuthPost.getId(), authInfo))
                .isInstanceOf(AuthorizationException.class);
    }

}