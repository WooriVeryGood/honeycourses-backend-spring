package org.wooriverygood.api.course.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.repository.CourseRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Courses> findAll() {
        return courseRepository.findAll();
    }
}
