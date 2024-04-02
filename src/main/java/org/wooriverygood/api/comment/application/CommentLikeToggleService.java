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

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentLikeToggleService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;


    public CommentLikeResponse likeComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndUsername(comment, authInfo.getUsername());

        if (commentLike.isEmpty()) {
            addCommentLike(comment, authInfo.getUsername());
            return createCommentLikeResponse(comment, true);
        }

        deleteCommentLike(comment, commentLike.get());
        return createCommentLikeResponse(comment, false);
    }

    private void addCommentLike(Comment comment, String username) {
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .username(username)
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
