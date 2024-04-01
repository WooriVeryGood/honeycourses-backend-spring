package org.wooriverygood.api.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.wooriverygood.api.review.domain.Review;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewResponse {
    private final Long reviewId;
    private final Long courseId;
    private final String reviewContent;
    private final String reviewTitle;
    private final String instructorName;
    private final String takenSemyr;
    private final String grade;
    private final int likeCount;
    private final LocalDateTime reviewTime;
    private final boolean isMine;
    private final boolean liked;
    private final boolean updated;

    @Builder
    public ReviewResponse(Long reviewId, Long courseId, String reviewContent, String reviewTitle, String instructorName, String takenSemyr, String grade, int likeCount, LocalDateTime reviewTime, boolean isMine, boolean liked, boolean updated) {
        this.reviewId = reviewId;
        this.courseId = courseId;
        this.reviewContent = reviewContent;
        this.reviewTitle = reviewTitle;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.grade = grade;
        this.likeCount = likeCount;
        this.reviewTime = reviewTime;
        this.isMine = isMine;
        this.liked = liked;
        this.updated = updated;
    }

    public static ReviewResponse of(Review review, boolean isMine, boolean liked) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .courseId(review.getCourse().getId())
                .reviewContent(review.getReviewContent())
                .reviewTitle(review.getReviewTitle())
                .instructorName(review.getInstructorName())
                .takenSemyr(review.getTakenSemyr())
                .grade(review.getGrade())
                .likeCount(review.getLikeCount())
                .reviewTime(review.getCreatedAt())
                .isMine(isMine)
                .liked(liked)
                .updated(review.isUpdated())
                .build();
    }
}
