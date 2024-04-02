package org.wooriverygood.api.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.report.exception.DuplicatedPostReportException;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.post.domain.Post;
import org.wooriverygood.api.post.exception.PostNotFoundException;
import org.wooriverygood.api.post.repository.PostRepository;
import org.wooriverygood.api.report.domain.PostReport;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.repository.PostReportRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PostReportService {

    private final PostRepository postRepository;

    private final PostReportRepository postReportRepository;


    public void reportPost(Long postId, ReportRequest request, AuthInfo authInfo) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        PostReport report = PostReport.builder()
                .post(post)
                .message(request.getMessage())
                .username(authInfo.getUsername())
                .build();

        checkIfAlreadyReport(post, authInfo);
        post.addReport(report);
        postRepository.increaseReportCount(postId);

        postReportRepository.save(report);
    }

    private void checkIfAlreadyReport(Post post, AuthInfo authInfo) {
        if (post.hasReportByUser(authInfo.getUsername()))
            throw new DuplicatedPostReportException();
    }

}
