package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostLike;
import org.wooriverygood.api.post.dto.PostLikeResponse;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeToggleService {

    private PostRepository postRepository;

    private PostLikeRepository postLikeRepository;

    public PostLikeResponse togglePostLike(long postId, AuthInfo authInfo) {
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
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }

}