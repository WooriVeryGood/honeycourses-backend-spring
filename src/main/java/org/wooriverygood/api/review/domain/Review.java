package org.wooriverygood.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.course.domain.Course;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @Column(name = "review_content", length = 1000)
    private String reviewContent;

    @Column(name = "review_title", length = 45)
    private String reviewTitle;

    @Column(name = "instructor_name", length = 45, nullable = false)
    private String instructorName;

    @Column(name = "taken_semyr", length = 45, nullable = false)
    private String takenSemyr;

    @Column(name = "grade", length = 45, nullable = false)
    private String grade;

    @Column(name = "author_email", length = 300)
    private String authorEmail;

    @Column(name = "like_count")
    @ColumnDefault("0")
    private int likeCount;

    @Column(name = "review_time")
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes;

    @ColumnDefault("false")
    private boolean updated;

    @Builder
    public Review(Long id, Course course, String reviewContent, String reviewTitle, String instructorName, String takenSemyr, String grade, String authorEmail, LocalDateTime createdAt, List<ReviewLike> reviewLikes, boolean updated) {
        this.id = id;
        this.course = course;
        this.reviewContent = reviewContent;
        this.reviewTitle = reviewTitle;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.grade = grade;
        this.authorEmail = authorEmail;
        this.createdAt = createdAt;
        this.reviewLikes = reviewLikes;
        this.updated = updated;
    }

    public boolean isSameAuthor(String author) {
        return this.authorEmail.equals(author);
    }

    public void validateAuthor(String author) {
        if (!this.authorEmail.equals(author)) {
            throw new AuthorizationException();
        }
    }

    public void addReviewLike(ReviewLike reviewLike) {
        reviewLikes.add(reviewLike);
    }

    public void deleteReviewLike(ReviewLike reviewLike) {
        reviewLikes.remove(reviewLike);
        reviewLike.delete();
    }

    public void updateReview(String title, String instructorName, String takenSemyr, String content, String grade, String author) {
        this.reviewTitle = title;
        this.reviewContent = content;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.grade = grade;
        updated = true;
        if (id <= 234) {
            authorEmail = author;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

}
