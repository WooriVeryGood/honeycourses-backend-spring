package org.wooriverygood.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentDeleteService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final MemberRepository memberRepository;


    public void deleteComment(Long commentId, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.validateAuthor(member);

        commentLikeRepository.deleteAllByComment(comment);
        deleteCommentOrReply(comment);
    }

    private void deleteCommentOrReply(Comment comment) {
        if (comment.isParent()) {
            deleteParent(comment);
            return;
        }
        deleteReply(comment);
    }

    private void deleteParent(Comment parent) {
        if (parent.hasNoReply()) {
            commentRepository.delete(parent);
            return;
        }
        parent.willBeDeleted();
    }

    private void deleteReply(Comment reply) {
        Comment parent = reply.getParent();
        parent.deleteReply(reply);
        commentRepository.delete(reply);

        if (parent.canDelete())
            commentRepository.delete(parent);
    }

}
