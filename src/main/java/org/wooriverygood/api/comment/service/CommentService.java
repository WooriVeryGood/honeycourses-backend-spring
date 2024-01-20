package org.wooriverygood.api.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.domain.CommentLike;
import org.wooriverygood.api.comment.dto.*;
import org.wooriverygood.api.comment.repository.CommentLikeRepository;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.advice.exception.CommentNotFoundException;
import org.wooriverygood.api.advice.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;


    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CommentLikeRepository commentLikeRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    public List<CommentResponse> findAllCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(CommentResponse::from).toList();
    }

    @Transactional
    public NewCommentResponse addComment(AuthInfo authInfo, Long postId, NewCommentRequest newCommentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                .content(newCommentRequest.getContent())
                .author(authInfo.getUsername())
                .post(post)
                .build();
        Comment saved = commentRepository.save(comment);

        return NewCommentResponse.builder()
                .comment_id(saved.getId())
                .content(saved.getContent())
                .author(saved.getAuthor())
                .build();
    }

    @Transactional
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
                .like_count(likeCount)
                .liked(liked)
                .build();
    }

    @Transactional
    public CommentDeleteResponse deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.validateAuthor(authInfo.getUsername());

        commentLikeRepository.deleteAllByComment(comment);
        commentRepository.delete(comment);

        return CommentDeleteResponse.builder()
                .comment_id(commentId)
                .build();
    }
}
