package org.wooriverygood.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeToggleService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final MemberRepository memberRepository;


    public CommentLikeResponse likeComment(Long commentId, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndMember(comment, member);

        if (commentLike.isEmpty()) {
            addCommentLike(comment, member);
            return createCommentLikeResponse(comment, true);
        }

        deleteCommentLike(comment, commentLike.get());
        return createCommentLikeResponse(comment, false);
    }

    private void addCommentLike(Comment comment, Member member) {
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .member(member)
                .build();

        comment.addCommentLike(commentLike);
        commentLikeRepository.save(commentLike);
        commentRepository.increaseLikeCount(comment.getId());
    }

    private void deleteCommentLike(Comment comment, CommentLike commentLike) {
        comment.deleteCommentLike(commentLike);
        commentRepository.decreaseLikeCount(comment.getId());
    }

    private CommentLikeResponse createCommentLikeResponse(Comment comment, boolean liked) {
        int likeCount = comment.getLikeCount() + (liked ? 1 : -1);
        return CommentLikeResponse.builder()
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }

}
