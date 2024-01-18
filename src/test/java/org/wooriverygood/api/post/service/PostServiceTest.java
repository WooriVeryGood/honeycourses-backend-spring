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
import org.wooriverygood.api.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.domain.PostLike;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.dto.NewPostResponse;
import org.wooriverygood.api.post.dto.PostLikeResponse;
import org.wooriverygood.api.post.dto.PostResponse;
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

    private final int POST_COUNT = 10;

    List<Post> posts = new ArrayList<>();

    Post singlePost = Post.builder()
            .id(6L)
            .category(PostCategory.OFFER)
            .title("title6")
            .content("content6")
            .author("author6")
            .comments(new ArrayList<>())
            .postLikes(new ArrayList<>())
            .build();

    AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();


    @BeforeEach
    void setUpPosts() {
        for (int i = 0; i < POST_COUNT; i++) {
            Post post = Post.builder()
                    .id((long) (i + 1))
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
    @DisplayName("모든 게시글을 불러온다.")
    void findAllPosts() {
        Mockito.when(postRepository.findAll())
                .thenReturn(posts);

        List<PostResponse> responses = postService.findAllPosts(authInfo);

        Assertions.assertThat(responses.size()).isEqualTo(POST_COUNT);
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
    @DisplayName("유효하지 않은 id를 이용하여 특정 게시글을 불러온다.")
    void findPostByIdAndThrowException() {
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
    @DisplayName("사용자 본인이 작성한 게시글을 불러온다.")
    void findMyPosts() {
        Mockito.when(postRepository.findByAuthor(any(String.class)))
                .thenReturn(posts);

        List<PostResponse> responses = postService.findMyPosts(authInfo);

        Assertions.assertThat(responses.get(0).isMine()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 게시글에 좋아요를 1 올린다.")
    void likePost_up() {
        Mockito.when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singlePost));

        PostLikeResponse response = postService.likePost(singlePost.getId(), authInfo);

        Assertions.assertThat(response.getLikeCount()).isEqualTo(singlePost.getLikeCount() + 1);
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

        Assertions.assertThat(response.getLikeCount()).isEqualTo(singlePost.getLikeCount() - 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(false);
    }

}