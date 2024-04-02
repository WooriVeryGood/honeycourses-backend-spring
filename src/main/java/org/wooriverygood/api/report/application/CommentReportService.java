package org.wooriverygood.api.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.report.exception.DuplicatedCommentReportException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.report.domain.CommentReport;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.repository.CommentReportRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentReportService {

    private final CommentRepository commentRepository;

    private final CommentReportRepository commentReportRepository;


    public void reportComment(Long commentId, ReportRequest request, AuthInfo authInfo) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        CommentReport report = CommentReport.builder()
                .comment(comment)
                .message(request.getMessage())
                .username(authInfo.getUsername())
                .build();

        checkIfAlreadyReport(comment, authInfo);
        comment.addReport(report);
        commentRepository.increaseReportCount(commentId);

        commentReportRepository.save(report);
    }

    private void checkIfAlreadyReport(Comment comment, AuthInfo authInfo) {
        if (comment.hasReportByUser(authInfo.getUsername()))
            throw new DuplicatedCommentReportException();
    }
}
