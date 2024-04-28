package org.wooriverygood.api.report.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.report.application.PostReportService;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.application.CommentReportService;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.global.auth.Login;

@RestController
@RequiredArgsConstructor
public class ReportApi {

    private final CommentReportService commentReportService;

    private final PostReportService postReportService;


    @PostMapping("/posts/{id}/report")
    public ResponseEntity<Void> reportPost(@PathVariable("id") Long postId,
                                           @Valid @RequestBody ReportRequest request,
                                           @Login AuthInfo authInfo) {
        postReportService.reportPost(postId, request, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments/{id}/report")
    public ResponseEntity<Void> reportComment(@PathVariable("id") Long commentId,
                                              @Valid @RequestBody ReportRequest request,
                                              @Login AuthInfo authInfo) {
        commentReportService.reportComment(commentId, request, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
