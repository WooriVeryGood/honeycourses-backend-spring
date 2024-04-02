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

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentFindService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;


    public CommentsResponse findAllCommentsByPostId(Long postId, AuthInfo authInfo) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        List<CommentResponse> responses = comments.stream()
                .map(comment -> convertToCommentResponse(comment, authInfo))
                .filter(response -> !Objects.isNull(response))
                .toList();
        return new CommentsResponse(responses);
    }

    private CommentResponse convertToCommentResponse(Comment comment, AuthInfo authInfo) {
        if (comment.isReply())
            return null;
        if (comment.isSoftRemoved())
            return CommentResponse.softRemovedOf(comment, convertToReplyResponses(comment, authInfo));

        boolean liked = commentLikeRepository.existsByCommentAndUsername(comment, authInfo.getUsername());
        return CommentResponse.of(comment, convertToReplyResponses(comment, authInfo), liked);
    }

    private List<ReplyResponse> convertToReplyResponses(Comment parent, AuthInfo authInfo) {
        return parent.getChildren()
                .stream()
                .map(reply -> {
                    boolean liked = commentLikeRepository.existsByCommentAndUsername(reply, authInfo.getUsername());
                    return ReplyResponse.from(reply, liked);
                })
                .toList();
    }

}
