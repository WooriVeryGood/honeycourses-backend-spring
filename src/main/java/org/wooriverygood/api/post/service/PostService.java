package org.wooriverygood.api.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.advice.exception.AuthorizationException;
import org.wooriverygood.api.advice.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.domain.PostLike;
import org.wooriverygood.api.post.dto.*;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;


    public PostService(PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
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
        PostCategory.parse(newPostRequest.getPost_category());
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

    @Transactional
    public PostLikeResponse likePost(Long postId, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Optional<PostLike> postLike = postLikeRepository.findByPostAndUsername(post, authInfo.getUsername());

        if (postLike.isEmpty()) {
            addPostLike(post, authInfo.getUsername());
            return createPostLikeResponse(post, true);
        }

        deletePostLike(post, postLike.get());
        return createPostLikeResponse(post, false);
    }

    private void addPostLike(Post post, String username) {
        PostLike newPostLike = PostLike.builder()
                .post(post)
                .username(username)
                .build();

        post.addPostLike(newPostLike);
        postLikeRepository.save(newPostLike);
        postRepository.increaseLikeCount(post.getId());
    }

    private void deletePostLike(Post post, PostLike postLike) {
        post.deletePostLike(postLike);
        postRepository.decreaseLikeCount(post.getId());
    }

    private PostLikeResponse createPostLikeResponse(Post post, boolean liked) {
        int likeCount = post.getLikeCount() + (liked ? 1 : -1);
        return PostLikeResponse.builder()
                .like_count(likeCount)
                .liked(liked)
                .build();
    }

    @Transactional
    public PostUpdateResponse updatePost(Long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
//        validateAuthor(authInfo, post);

        post.updateTitle(postUpdateRequest.getPost_title());
        post.updateContent(postUpdateRequest.getPost_content());

        return PostUpdateResponse.builder()
                .post_id(post.getId())
                .post_title(post.getTitle())
                .post_content(post.getContent())
                .build();
    }

    private void validateAuthor(AuthInfo authInfo, Post post) {
        if (!post.isSameAuthor(authInfo.getUsername())) throw new AuthorizationException();
    }
}
