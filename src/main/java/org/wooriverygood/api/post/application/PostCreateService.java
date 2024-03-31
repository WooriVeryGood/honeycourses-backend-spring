package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@RequiredArgsConstructor
public class PostCreateService {

    private final PostRepository postRepository;

    @Transactional
    public void addPost(AuthInfo authInfo, NewPostRequest newPostRequest) {
        PostCategory.parse(newPostRequest.getPostCategory());
        Post post = createPost(authInfo, newPostRequest);
        postRepository.save(post);
    }

    private Post createPost(AuthInfo authInfo, NewPostRequest newPostRequest) {
        return Post.builder()
                .title(newPostRequest.getPostTitle())
                .content(newPostRequest.getPostContent())
                .category(PostCategory.parse(newPostRequest.getPostCategory()))
                .author(authInfo.getUsername())
                .build();
    }

}