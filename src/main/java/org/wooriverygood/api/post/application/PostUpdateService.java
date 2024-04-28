package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.dto.PostUpdateRequest;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class PostUpdateService {

    private final PostRepository postRepository;

    private final MemberRepository memberRepository;


    public void updatePost(long postId, PostUpdateRequest postUpdateRequest, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        post.validateAuthor(member);

        post.updateTitle(postUpdateRequest.getPostTitle());
        post.updateContent(postUpdateRequest.getPostContent());
    }

}
