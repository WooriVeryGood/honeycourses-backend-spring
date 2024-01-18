package org.wooriverygood.api.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.dto.CommentResponse;
import org.wooriverygood.api.comment.dto.NewCommentRequest;
import org.wooriverygood.api.comment.dto.NewCommentResponse;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
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
}
