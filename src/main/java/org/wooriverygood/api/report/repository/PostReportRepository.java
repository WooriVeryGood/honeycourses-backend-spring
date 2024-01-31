package org.wooriverygood.api.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.report.domain.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
}
