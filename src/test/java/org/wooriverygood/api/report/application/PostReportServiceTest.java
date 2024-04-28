package org.wooriverygood.api.report.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.member.domain.Member;
import org.wooriverygood.api.member.repository.MemberRepository;
import org.wooriverygood.api.post.application.PostServiceTest;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.report.domain.PostReport;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.exception.DuplicatedPostReportException;
import org.wooriverygood.api.report.repository.PostReportRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostReportServiceTest extends PostServiceTest {

    @InjectMocks
    private PostReportService postReportService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostReportRepository postReportRepository;

    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("유효한 id를 통해 특정 게시글을 신고한다.")
    void reportPost() {
        ReportRequest request = new ReportRequest("report message");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));

        postReportService.reportPost(1L, request, authInfo);

        assertAll(
                () -> verify(postRepository).increaseReportCount(post.getId()),
                () -> verify(postReportRepository).save(any(PostReport.class))
        );
    }

    @Test
    @DisplayName("신고하려는 게시글 id가 유효하지 않으면 예외를 발생한다.")
    void reportPost_exception_invalidId() {
        ReportRequest request = new ReportRequest("report message");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenThrow(new PostNotFoundException());

        assertThatThrownBy(() -> postReportService.reportPost(post.getId(), request, authInfo))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("동일한 게시글을 한 번 이상 신고하면 예외를 발생한다.")
    void reportPost_exception_duplicated() {
        ReportRequest request = new ReportRequest("report message");
        when(memberRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(member));
        when(postRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(post));
        postReportService.reportPost(post.getId(), request, authInfo);

        assertThatThrownBy(() -> postReportService.reportPost(post.getId(), request, authInfo))
                .isInstanceOf(DuplicatedPostReportException.class);
    }

}