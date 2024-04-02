package org.wooriverygood.api.report.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.advice.exception.CommentNotFoundException;
import org.wooriverygood.api.report.domain.CommentReport;
import org.wooriverygood.api.report.exception.DuplicatedCommentReportException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.repository.CommentReportRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentReportServiceTest extends MockTest {

    @InjectMocks
    private CommentReportService commentReportService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentReportRepository commentReportRepository;

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("22432-12312-3531")
            .username("22432-12312-3531")
            .build();

    @Mock
    private Comment comment;


    @Test
    @DisplayName("유효한 id를 통해 특정 댓글을 신고한다.")
    void reportComment() {
        ReportRequest request = new ReportRequest("report message");
        when(comment.hasReportByUser(anyString()))
                .thenReturn(false);
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentReportService.reportComment(1L, request, authInfo);

        verify(comment).hasReportByUser(authInfo.getUsername());
        verify(comment).addReport(any(CommentReport.class));
        verify(commentRepository).increaseReportCount(anyLong());
        verify(commentReportRepository).save(any(CommentReport.class));
    }

    @Test
    @DisplayName("신고하려는 댓글의 id가 유효하지 않으면 예외를 발생한다.")
    void reportComment_exception_invalidId() {
        ReportRequest request = new ReportRequest("report message");

        when(commentRepository.findById(anyLong()))
                .thenThrow(new CommentNotFoundException());

        assertThatThrownBy(() -> commentReportService.reportComment(1L, request, authInfo))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("동일한 댓글을 한 번 이상 신고하면 예외를 발생한다.")
    void reportComment_exception_duplicated() {
        ReportRequest request = new ReportRequest("report message");
        when(comment.hasReportByUser(anyString()))
                .thenReturn(true);
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        assertThatThrownBy(() -> commentReportService.reportComment(1L, request, authInfo))
                .isInstanceOf(DuplicatedCommentReportException.class);
    }

}