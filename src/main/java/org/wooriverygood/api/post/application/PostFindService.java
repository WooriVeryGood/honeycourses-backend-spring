package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.PostDetailResponse;
import org.wooriverygood.api.post.dto.PostResponse;
import org.wooriverygood.api.post.dto.PostsResponse;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostFindService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;


    public PostsResponse findPosts(AuthInfo authInfo, Pageable pageable, String postCategory) {
        Page<Post> page = findPostsPage(pageable, postCategory);
        return convertToPostsResponse(authInfo, page);
    }

    private Page<Post> findPostsPage(Pageable pageable, String postCategory) {
        if (postCategory.isEmpty())
            return postRepository.findAllByOrderByIdDesc(pageable);
        PostCategory category = PostCategory.parse(postCategory);
        return postRepository.findAllByCategoryOrderByIdDesc(category, pageable);
    }

    @Transactional
    public PostDetailResponse findPostById(long postId, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        boolean liked = postLikeRepository.existsByPostAndUsername(post, authInfo.getUsername());
        postRepository.increaseViewCount(postId);
        return PostDetailResponse.of(post, liked);
    }

    public PostsResponse findMyPosts(AuthInfo authInfo, Pageable pageable) {
        Page<Post> page = postRepository.findByAuthorOrderByIdDesc(authInfo.getUsername(), pageable);
        return convertToPostsResponse(authInfo, page);
    }

    private PostsResponse convertToPostsResponse(AuthInfo authInfo, Page<Post> page) {
        List<PostResponse> posts = page.getContent().stream()
                .map(post -> {
                    boolean liked = postLikeRepository.existsByPostAndUsername(post, authInfo.getUsername());
                    return PostResponse.of(post, liked);
                })
                .toList();

        return PostsResponse.builder()
                .posts(posts)
                .totalPageCount(page.getTotalPages())
                .totalPostCount(page.getTotalElements())
                .build();
    }

}