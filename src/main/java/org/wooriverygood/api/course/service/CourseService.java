package org.wooriverygood.api.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.dto.CourseResponse;
import org.wooriverygood.api.course.repository.CourseRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream().map(CourseResponse::from).toList();
    }
}
