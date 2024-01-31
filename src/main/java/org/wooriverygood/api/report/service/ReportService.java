package org.wooriverygood.api.report.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.advice.exception.CommentNotFoundException;
import org.wooriverygood.api.advice.exception.DuplicatedCommentReportException;
import org.wooriverygood.api.advice.exception.DuplicatedPostReportException;
import org.wooriverygood.api.advice.exception.PostNotFoundException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.report.domain.CommentReport;
import org.wooriverygood.api.report.domain.PostReport;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.repository.CommentReportRepository;
import org.wooriverygood.api.report.repository.PostReportRepository;
import org.wooriverygood.api.support.AuthInfo;

@Service
@Transactional
public class ReportService {

    private final CommentRepository commentRepository;

    private final CommentReportRepository commentReportRepository;

    private final PostRepository postRepository;

    private final PostReportRepository postReportRepository;


    public ReportService(CommentRepository commentRepository, CommentReportRepository commentReportRepository, PostRepository postRepository, PostReportRepository postReportRepository) {
        this.commentRepository = commentRepository;
        this.commentReportRepository = commentReportRepository;
        this.postRepository = postRepository;
        this.postReportRepository = postReportRepository;
    }

    public void reportPost(Long postId, ReportRequest request, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        PostReport report = PostReport.builder()
                .post(post)
                .message(request.getMessage())
                .username(authInfo.getUsername())
                .build();

        checkAlreadyReport(post, authInfo);
        post.addReport(report);
        postRepository.increaseReportCount(postId);

        postReportRepository.save(report);
    }

    private void checkAlreadyReport(Post post, AuthInfo authInfo) {
        if (post.hasReportByUser(authInfo.getUsername()))
            throw new DuplicatedPostReportException();
    }

    public void reportComment(Long commentId, ReportRequest request, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        CommentReport report = CommentReport.builder()
                .comment(comment)
                .message(request.getMessage())
                .username(authInfo.getUsername())
                .build();

        checkAlreadyReport(comment, authInfo);
        comment.addReport(report);
        commentRepository.increaseReportCount(commentId);

        commentReportRepository.save(report);
    }

    private void checkAlreadyReport(Comment comment, AuthInfo authInfo) {
        if (comment.hasReportByUser(authInfo.getUsername()))
            throw new DuplicatedCommentReportException();
    }
}
