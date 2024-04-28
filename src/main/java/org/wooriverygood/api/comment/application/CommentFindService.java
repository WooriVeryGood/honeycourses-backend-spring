package org.wooriverygood.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentResponse;
import org.wooriverygood.api.comment.dto.CommentsResponse;
import org.wooriverygood.api.comment.dto.ReplyResponse;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.exception.MemberNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentFindService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final MemberRepository memberRepository;


    public CommentsResponse findAllCommentsByPostId(Long postId, AuthInfo authInfo) {
        Member member = memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentResponse> responses = comments.stream()
                .map(comment -> convertToCommentResponse(comment, member))
                .filter(response -> !Objects.isNull(response))
                .toList();
        return new CommentsResponse(responses);
    }

    private CommentResponse convertToCommentResponse(Comment comment, Member member) {
        if (comment.isReply())
            return null;
        if (comment.isSoftRemoved())
            return CommentResponse.softRemovedOf(comment, convertToReplyResponses(comment, member), comment.sameAuthor(member));

        boolean liked = commentLikeRepository.existsByCommentAndMember(comment, member);
        return CommentResponse.of(comment, convertToReplyResponses(comment, member), liked, comment.sameAuthor(member));
    }

    private List<ReplyResponse> convertToReplyResponses(Comment parent, Member member) {
        return parent.getReplies()
                .stream()
                .map(reply -> {
                    boolean liked = commentLikeRepository.existsByCommentAndMember(reply, member);
                    return ReplyResponse.of(reply, liked, reply.sameAuthor(member));
                })
                .toList();
    }

}
