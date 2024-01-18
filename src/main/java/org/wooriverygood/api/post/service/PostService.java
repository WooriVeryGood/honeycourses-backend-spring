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
import org.wooriverygood.api.support.AuthInfo;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;


    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostResponse> findAllPosts(AuthInfo authInfo) {
        List<Post> posts = postRepository.findAll();

        if (authInfo.getUsername() == null)
            return posts.stream()
                    .map(post -> PostResponse.from(post, false))
                    .toList();
        return posts.stream()
                .map(post -> PostResponse.from(post, post.isSameAuthor(authInfo.getUsername())))
                .toList();
    }

    public PostResponse findPostById(Long postId, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return PostResponse.from(post, post.isSameAuthor(authInfo.getUsername()));
    }

    @Transactional
    public NewPostResponse addPost(AuthInfo authInfo, NewPostRequest newPostRequest) {
        Post post = createPost(authInfo, newPostRequest);
        Post saved = postRepository.save(post);
        return createResponse(saved);
    }

    private Post createPost(AuthInfo authInfo, NewPostRequest newPostRequest) {
        return Post.builder()
                .title(newPostRequest.getPost_title())
                .content(newPostRequest.getPost_content())
                .category(PostCategory.parse(newPostRequest.getPost_category()))
                .author(authInfo.getUsername())
                .build();
    }

    private NewPostResponse createResponse(Post post) {
        return NewPostResponse.builder()
                .post_id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory().getValue())
                .author(post.getAuthor())
                .build();
    }

    public List<PostResponse> findMyPosts(AuthInfo authInfo) {
        List<Post> posts = postRepository.findByAuthor(authInfo.getUsername());
        return posts.stream().map(post -> PostResponse.from(post, true)).toList();
    }
}
