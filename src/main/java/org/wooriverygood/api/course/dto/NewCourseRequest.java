package org.wooriverygood.api.course.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NewCourseRequest {

    private final String courseName;

    private final double courseCredit;

    private final String courseCategory;

    private final String kaikeYuanxi;

    private final int isYouguan;


    @Builder
    public NewCourseRequest(String courseName, double courseCredit, String courseCategory, String kaikeYuanxi, int isYouguan) {
        this.courseName = courseName;
        this.courseCredit = courseCredit;
        this.courseCategory = courseCategory;
        this.kaikeYuanxi = kaikeYuanxi;
        this.isYouguan = isYouguan;
    }

}
