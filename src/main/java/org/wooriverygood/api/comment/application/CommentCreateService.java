package org.wooriverygood.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.comment.exception.ReplyDepthException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.NewCommentRequest;
import org.wooriverygood.api.comment.dto.NewReplyRequest;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCreateService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    private final MemberRepository memberRepository;


    public void addComment(AuthInfo authInfo, Long postId, NewCommentRequest request) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment);
    }

    public void addReply(Long commentId, NewReplyRequest request, AuthInfo authInfo) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!parent.isParent())
            throw new ReplyDepthException();

        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Comment child = Comment.builder()
                .content(request.getContent())
                .post(parent.getPost())
                .parent(parent)
                .member(member)
                .build();
        parent.addReply(child);

        commentRepository.save(child);
    }

}
