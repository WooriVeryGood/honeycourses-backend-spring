package org.wooriverygood.api.post.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

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

    @BeforeEach
    void setUpPosts() {
        for (int i = 0; i < POST_COUNT; i++) {
            Post post = Post.builder()
                    .id((long) (i + 1))
                    .category(PostCategory.FREE)
                    .title("title" + i)
                    .content("content" + i)
                    .author("author" + i)
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

        List<PostResponse> responses = postService.findAllPosts();

        Assertions.assertThat(responses.size()).isEqualTo(POST_COUNT);
    }

    @Test
    @DisplayName("유효한 id를 이용하여 특정 게시글을 불러온다.")
    void findPostById() {
        Mockito.when(postRepository.findById(any()))
                .thenReturn(Optional.ofNullable(singlePost));

        PostResponse response = postService.findPostById(6L);

        Assertions.assertThat(response).isNotNull();
    }

}