package org.wooriverygood.api.course.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCourseRequest {
    private final String course_name;
    private final int course_credit;
    private final String course_category;
    private final String kaikeYuanxi;
    private final int isYouguan;

    @Builder
    public NewCourseRequest(String course_name, int course_credit, String course_category, String kaikeYuanxi, int isYouguan) {
        this.course_name = course_name;
        this.course_credit = course_credit;
        this.course_category = course_category;
        this.kaikeYuanxi = kaikeYuanxi;
        this.isYouguan = isYouguan;
    }
}
