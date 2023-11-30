package org.wooriverygood.api.course.dto;

import lombok.Getter;
import org.wooriverygood.api.course.domain.Courses;

@Getter
public class CourseResponse {
    private final String course_category;
    private final int course_credit;
    private final String course_name;
    private final int isYouguan;
    private final String kaikeYuanxi;
    public CourseResponse(Courses course) {
        this.course_category = course.getCourse_category();
        this.course_credit = course.getCourse_credit();
        this.course_name = course.getCourse_name();
        this.isYouguan = course.getIsYouguan();
        this.kaikeYuanxi = course.getKaikeYuanxi();
    }
}
