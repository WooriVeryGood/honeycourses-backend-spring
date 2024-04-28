package org.wooriverygood.api.report.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.comment.application.CommentServiceTest;
import org.wooriverygood.api.comment.exception.CommentNotFoundException;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.report.domain.CommentReport;
import org.wooriverygood.api.report.exception.DuplicatedCommentReportException;
import org.wooriverygood.api.comment.domain.Comment;
import org.wooriverygood.api.comment.repository.CommentRepository;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.repository.CommentReportRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentReportServiceTest extends CommentServiceTest {

    @InjectMocks
    private CommentReportService commentReportService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentReportRepository commentReportRepository;

    @Mock
    private MemberRepository memberRepository;

    private Post post;


    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(6L)
                .category(PostCategory.OFFER)
                .title("title6")
                .content("content6")
                .member(member)
                .comments(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 댓글을 신고한다.")
    void reportComment() {
        ReportRequest request = new ReportRequest("report message");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentReportService.reportComment(comment.getId(), request, authInfo);

        assertAll(
                () -> assertThat(comment.getReports().size()).isEqualTo(1),
                () -> verify(commentRepository).increaseReportCount(comment.getId()),
                () -> verify(commentReportRepository).save(any(CommentReport.class))
        );
    }

    @Test
    @DisplayName("동일한 댓글을 한 번 이상 신고하면 예외를 발생한다.")
    void reportComment_exception_duplicated() {
        ReportRequest request = new ReportRequest("report message");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(comment));

        commentReportService.reportComment(comment.getId(), request, authInfo);

        assertThatThrownBy(() -> commentReportService.reportComment(1L, request, authInfo))
                .isInstanceOf(DuplicatedCommentReportException.class);
    }

}