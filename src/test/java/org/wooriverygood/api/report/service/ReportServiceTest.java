package org.wooriverygood.api.report.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wooriverygood.api.advice.exception.DuplicatedPostReportException;
import org.wooriverygood.api.advice.exception.PostNotFoundException;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.domain.PostCategory;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.repository.PostReportRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostReportRepository postReportRepository;

    private AuthInfo testAuthInfo;

    private Post testPost;


    @BeforeEach
    void setUp() {
        testAuthInfo = AuthInfo.builder()
                .sub("22432-12312-3531")
                .username("22432-12312-3531")
                .build();

        testPost = Post.builder()
                .id(1L)
                .category(PostCategory.OFFER)
                .title("post title")
                .content("post content")
                .author(testAuthInfo.getUsername())
                .comments(new ArrayList<>())
                .postLikes(new ArrayList<>())
                .reports(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 게시글을 신고한다.")
    void reportPost() {
        ReportRequest request = ReportRequest.builder()
                .message("report message")
                .build();

        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(testPost));

        reportService.reportPost(1L, request, testAuthInfo);

        assertThat(testPost.getReports().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("신고하려는 게시글 id가 유효하지 않으면 예외를 발생한다.")
    void reportPost_exception_invalidId() {
        ReportRequest request = ReportRequest.builder()
                .message("report message")
                .build();

        when(postRepository.findById(any(Long.class)))
                .thenThrow(new PostNotFoundException());

        assertThatThrownBy(() -> reportService.reportPost(1L, request, testAuthInfo))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("동일한 게시글을 한 번 이상 신고하면 예외를 발생한다.")
    void reportPost_exception_duplicated() {
        ReportRequest request = ReportRequest.builder()
                .message("report message")
                .build();

        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(testPost));
        reportService.reportPost(1L, request, testAuthInfo);

        assertThatThrownBy(() -> reportService.reportPost(1L, request, testAuthInfo))
                .isInstanceOf(DuplicatedPostReportException.class);
    }

}