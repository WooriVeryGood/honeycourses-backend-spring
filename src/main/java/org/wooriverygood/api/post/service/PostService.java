package org.wooriverygood.api.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.dto.NewPostResponse;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;


    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponse> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PostResponse findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostResponse.from(post);
    }

    public NewPostResponse addPost(NewPostRequest newPostRequest) {
        Post post = Post.builder()
                .title(newPostRequest.getPost_title())
                .content(newPostRequest.getPost_content())
                .category(PostCategory.parse(newPostRequest.getPost_category()))
                .author(newPostRequest.getEmail())
                .build();
        Post saved = postRepository.save(post);

        return NewPostResponse.builder()
                .post_id(saved.getId())
                .title(saved.getTitle())
                .category(saved.getCategory().getValue())
                .author(saved.getAuthor())
                .build();
    }
}
