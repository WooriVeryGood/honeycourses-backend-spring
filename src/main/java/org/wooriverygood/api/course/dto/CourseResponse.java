package org.wooriverygood.api.course.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.course.domain.Course;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseResponse {

    private final Long courseId;

    private final String courseCategory;

    private final double courseCredit;

    private final String courseName;

    private final int isYouguan;

    private final String kaikeYuanxi;

    private final int reviewCount;


    @Builder
    public CourseResponse(Long courseId, String courseCategory, double courseCredit, String courseName, int isYouguan, String kaikeYuanxi, int reviewCount) {
        this.courseId = courseId;
        this.courseCategory = courseCategory;
        this.courseCredit = courseCredit;
        this.courseName = courseName;
        this.isYouguan = isYouguan;
        this.kaikeYuanxi = kaikeYuanxi;
        this.reviewCount = reviewCount;
    }

    public static CourseResponse of(Course course) {
        return CourseResponse.builder()
                .courseId(course.getId())
                .courseCategory(course.getCategory())
                .courseCredit(course.getCredit())
                .courseName(course.getName())
                .isYouguan(course.getIsYouguan())
                .kaikeYuanxi(course.getKaikeYuanxi())
                .reviewCount(course.getReviewCount())
                .build();
    }

}
