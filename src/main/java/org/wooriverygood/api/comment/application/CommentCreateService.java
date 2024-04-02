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
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCreateService {

    private final PostRepository postRepository;

    private final CommentRepository commentRepository;


    public void addComment(AuthInfo authInfo, Long postId, NewCommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(authInfo.getUsername())
                .post(post)
                .build();

        commentRepository.save(comment);
    }

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
