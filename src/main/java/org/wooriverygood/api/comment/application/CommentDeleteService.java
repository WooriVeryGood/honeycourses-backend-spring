package org.wooriverygood.api.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentDeleteService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;


    public void deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.validateAuthor(authInfo.getUsername());

        commentLikeRepository.deleteAllByComment(comment);
        deleteCommentOrReply(comment);
    }

    private void deleteCommentOrReply(Comment comment) {
        if (comment.isParent()) {
            deleteParent(comment);
            return;
        }
        deleteChild(comment);
    }

    private void deleteParent(Comment parent) {
        if (parent.hasNoReply()) {
            commentRepository.delete(parent);
            return;
        }
        parent.willBeDeleted();
    }

    private void deleteChild(Comment reply) {
        Comment parent = reply.getParent();
        parent.deleteChild(reply);
        commentRepository.delete(reply);

        if (parent.canDelete())
            commentRepository.delete(parent);
    }

}
