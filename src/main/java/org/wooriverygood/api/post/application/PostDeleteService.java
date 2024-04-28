package org.wooriverygood.api.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostLikeRepository;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class PostDeleteService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;

    private final MemberRepository memberRepository;


   public void deletePost(AuthInfo authInfo, long postId) {
       Member member = memberRepository.findById(authInfo.getMemberId())
               .orElseThrow(MemberNotFoundException::new);
       Post post = postRepository.findById(postId)
               .orElseThrow(PostNotFoundException::new);
       post.validateAuthor(member);

       deleteCommentAndPostLike(post);
       postRepository.delete(post);
   }

   private void deleteCommentAndPostLike(Post post) {
       commentRepository.deleteAllByPost(post);
       postLikeRepository.deleteAllByPost(post);
   }

}
