package org.wooriverygood.api.report.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.wooriverygood.api.report.dto.ReportRequest;
import org.wooriverygood.api.report.service.ReportService;
import org.wooriverygood.api.support.AuthInfo;
import org.wooriverygood.api.support.Login;

@RestController
public class ReportController {

    private final ReportService reportService;


    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @PostMapping("/posts/{id}/report")
    public ResponseEntity<Void> reportPost(@PathVariable("id") Long postId,
                                           @Valid @RequestBody ReportRequest request,
                                           @Login AuthInfo authInfo) {
        reportService.reportPost(postId, request, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/comments/{id}/report")
    public ResponseEntity<Void> reportComment(@PathVariable("id") Long commentId,
                                              @Valid @RequestBody ReportRequest request,
                                              @Login AuthInfo authInfo) {
        reportService.reportComment(commentId, request, authInfo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
