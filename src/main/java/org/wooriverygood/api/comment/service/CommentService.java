package org.wooriverygood.api.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.advice.exception.ReplyDepthException;
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
import java.util.Objects;
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

    public List<CommentResponse> findAllComments(Long postId, AuthInfo authInfo) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(comment -> convertToCommentResponse(comment, authInfo))
                .filter(response -> !Objects.isNull(response))
                .toList();
    }

    private CommentResponse convertToCommentResponse(Comment comment, AuthInfo authInfo) {
        if (comment.isReply())
            return null;
        if (comment.isSoftRemoved())
            return CommentResponse.softRemovedFrom(comment, convertToReplyResponses(comment, authInfo));

        boolean liked = commentLikeRepository.existsByCommentAndUsername(comment, authInfo.getUsername());
        return CommentResponse.from(comment, convertToReplyResponses(comment, authInfo), liked);
    }

    private List<ReplyResponse> convertToReplyResponses(Comment parent, AuthInfo authInfo) {
        return parent.getChildren().stream().map(reply -> {
            boolean liked = commentLikeRepository.existsByCommentAndUsername(reply, authInfo.getUsername());
            return ReplyResponse.from(reply, liked);
        }).toList();
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
    public CommentUpdateResponse updateComment(Long commentId, CommentUpdateRequest request, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        comment.validateAuthor(authInfo.getUsername());

        comment.updateContent(request.getContent());

        return CommentUpdateResponse.builder()
                .comment_id(comment.getId())
                .build();
    }

    @Transactional
    public CommentDeleteResponse deleteComment(Long commentId, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        comment.validateAuthor(authInfo.getUsername());

        commentLikeRepository.deleteAllByComment(comment);
        deleteCommentOrReply(comment);

        return CommentDeleteResponse.builder()
                .comment_id(commentId)
                .build();
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

    @Transactional
    public void addReply(Long commentId, NewReplyRequest request, AuthInfo authInfo) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!parent.isParent())
            throw new ReplyDepthException();

        Comment child = Comment.builder()
                .content(request.getContent())
                .author(authInfo.getUsername())
                .post(parent.getPost())
                .parent(parent)
                .build();
        parent.addChildren(child);

        commentRepository.save(child);
    }
}
