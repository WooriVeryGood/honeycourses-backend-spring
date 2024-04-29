package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
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
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostFindService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;

    private final MemberRepository memberRepository;


    public PostsResponse findPosts(AuthInfo authInfo, Pageable pageable, String postCategory) {
        Page<Post> page = findPostsPage(pageable, postCategory);
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        return convertToPostsResponse(member, page);
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
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        boolean liked = postLikeRepository.existsByPostAndMember(post, member);
        boolean isMine = post.sameAuthor(member);
        postRepository.increaseViewCount(postId);
        return PostDetailResponse.of(post, member.getId(), liked, isMine);
    }

    public PostsResponse findMyPosts(AuthInfo authInfo, Pageable pageable) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Page<Post> page = postRepository.findByMemberOrderByIdDesc(member, pageable);
        return convertToPostsResponse(member, page);
    }

    private PostsResponse convertToPostsResponse(Member member, Page<Post> page) {
        List<PostResponse> posts = page.getContent().stream()
                .map(post -> {
                    boolean liked = postLikeRepository.existsByPostAndMember(post, member);
                    boolean isMine = post.sameAuthor(member);
                    return PostResponse.of(post, liked, isMine);
                })
                .toList();

        return PostsResponse.builder()
                .posts(posts)
                .totalPageCount(page.getTotalPages())
                .totalPostCount(page.getTotalElements())
                .build();
    }

}