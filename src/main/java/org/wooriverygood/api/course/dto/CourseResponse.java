package org.wooriverygood.api.course.dto;

import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.course.domain.Courses;

@Getter
public class CourseResponse {
    private final Long course_id;
    private final String course_category;
    private final int course_credit;
    private final String course_name;
    private final int isYouguan;
    private final String kaikeYuanxi;
    private final int reviewCount;

    @Builder
    public CourseResponse(Long course_id, String course_category, int course_credit, String course_name, int isYouguan, String kaikeYuanxi, int reviewCount) {
        this.course_id = course_id;
        this.course_category = course_category;
        this.course_credit = course_credit;
        this.course_name = course_name;
        this.isYouguan = isYouguan;
        this.kaikeYuanxi = kaikeYuanxi;
        this.reviewCount = reviewCount;
    }

    public static CourseResponse from(Courses course) {
        return CourseResponse.builder()
                .course_id(course.getId())
                .course_category(course.getCourse_category())
                .course_credit(course.getCourse_credit())
                .course_name(course.getCourse_name())
                .isYouguan(course.getIsYouguan())
                .kaikeYuanxi(course.getKaikeYuanxi())
                .reviewCount(course.getReviewCount())
                .build();
    }
}
