package org.wooriverygood.api.post.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    private final CommentRepository commentRepository;



    public PostService(PostRepository postRepository, PostLikeRepository postLikeRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.commentRepository = commentRepository;
    }

    public PostsResponse findPosts(AuthInfo authInfo, Pageable pageable) {
        Page<Post> page = postRepository.findAllByOrderByIdDesc(pageable);
        return convertToPostsResponse(authInfo, page);
    }

    public PostResponse findPostById(Long postId, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        boolean liked = postLikeRepository.existsByPostAndUsername(post, authInfo.getUsername());
        return PostResponse.from(post, liked);
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

    public PostsResponse findMyPosts(AuthInfo authInfo, Pageable pageable) {
        Page<Post> page = postRepository.findByAuthorOrderByIdDesc(authInfo.getUsername(), pageable);
        return convertToPostsResponse(authInfo, page);
    }

    private PostsResponse convertToPostsResponse(AuthInfo authInfo, Page<Post> page) {
        List<PostResponse> posts = page.getContent().stream()
                .map(post -> {
                    boolean liked = postLikeRepository.existsByPostAndUsername(post, authInfo.getUsername());
                    return PostResponse.from(post, liked);
                })
                .toList();

        return PostsResponse.builder()
                .posts(posts)
                .totalPageCount(page.getTotalPages())
                .totalPostCount(page.getTotalElements())
                .build();
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
        post.validateAuthor(authInfo.getUsername());

        post.updateTitle(postUpdateRequest.getPost_title());
        post.updateContent(postUpdateRequest.getPost_content());

        return PostUpdateResponse.builder()
                .post_id(post.getId())
                .build();
    }

    @Transactional
    public PostDeleteResponse deletePost(Long postId, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.validateAuthor(authInfo.getUsername());

        commentRepository.deleteAllByPost(post);
        postLikeRepository.deleteAllByPost(post);

        postRepository.delete(post);

        return PostDeleteResponse.builder()
                .post_id(postId)
                .build();
    }

    public PostsResponse findPostsByCategory(AuthInfo authInfo, Pageable pageable, String postCategory) {
        PostCategory category = PostCategory.parse(postCategory);
        Page<Post> page = postRepository.findAllByCategoryOrderByIdDesc(category, pageable);
        return convertToPostsResponse(authInfo, page);
    }
}
