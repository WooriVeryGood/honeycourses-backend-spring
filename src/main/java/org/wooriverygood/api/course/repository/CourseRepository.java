package org.wooriverygood.api.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.course.domain.Courses;

public interface CourseRepository extends JpaRepository<Courses, Long> {
}
