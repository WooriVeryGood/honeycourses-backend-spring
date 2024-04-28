package org.wooriverygood.api.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE courses SET review_count = review_count + 1 WHERE course_id = :courseId", nativeQuery = true)
    void increaseReviewCount(Long courseId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE courses SET review_count = review_count - 1 WHERE course_id = :courseId", nativeQuery = true)
    void decreaseReviewCount(Long courseId);

}
