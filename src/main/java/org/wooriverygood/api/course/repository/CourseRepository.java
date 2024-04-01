package org.wooriverygood.api.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wooriverygood.api.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
