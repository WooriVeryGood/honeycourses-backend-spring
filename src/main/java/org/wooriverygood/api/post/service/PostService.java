package org.wooriverygood.api.post.service;

import org.springframework.stereotype.Service;
import org.wooriverygood.api.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;


    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponse> findAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(PostResponse::from).toList();
    }

    public PostResponse findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostResponse.from(post);
    }
}
