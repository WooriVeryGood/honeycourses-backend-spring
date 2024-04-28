package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.dto.NewPostRequest;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCreateService {

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;

    public void addPost(AuthInfo authInfo, NewPostRequest newPostRequest) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        PostCategory.parse(newPostRequest.getPostCategory());
        Post post = createPost(member, newPostRequest);
        postRepository.save(post);
    }

    private Post createPost(Member member, NewPostRequest newPostRequest) {
        return Post.builder()
                .title(newPostRequest.getPostTitle())
                .content(newPostRequest.getPostContent())
                .category(PostCategory.parse(newPostRequest.getPostCategory()))
                .member(member)
                .build();
    }

}