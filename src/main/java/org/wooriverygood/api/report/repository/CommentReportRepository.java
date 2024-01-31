package org.wooriverygood.api.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.report.domain.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}
