package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.dto.PostUpdateRequest;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@RequiredArgsConstructor
public class PostUpdateService {

    private final PostRepository postRepository;

    @Transactional
    public void updatePost(long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.validateAuthor(authInfo.getUsername());

        post.updateTitle(postUpdateRequest.getPostTitle());
        post.updateContent(postUpdateRequest.getPostContent());
    }

}
