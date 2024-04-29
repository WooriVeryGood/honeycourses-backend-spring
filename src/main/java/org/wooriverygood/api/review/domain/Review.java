package org.wooriverygood.api.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

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
    public Review(Long id, Course course, String reviewContent, String reviewTitle, String instructorName,
                  String takenSemyr, String grade, LocalDateTime createdAt, Member member,
                  List<ReviewLike> reviewLikes, boolean updated) {
        this.id = id;
        this.course = course;
        this.reviewContent = reviewContent;
        this.reviewTitle = reviewTitle;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.grade = grade;
        this.createdAt = createdAt;
        this.reviewLikes = reviewLikes;
        this.updated = updated;
        this.member = member;
    }

    public boolean sameAuthor(Member member) {
        return this.member.isSame(member);
    }

    public void validateAuthor(Member member) {
        this.member.verify(member);
    }

    public void addReviewLike(ReviewLike reviewLike) {
        reviewLikes.add(reviewLike);
    }

    public void deleteReviewLike(ReviewLike reviewLike) {
        reviewLikes.remove(reviewLike);
        reviewLike.delete();
    }

    public void updateTitle(String title) {
        reviewTitle = title;
        updated = true;
    }

    public void updateContent(String content) {
        reviewContent = content;
        updated = true;
    }

    public void updateTakenSemyr(String takenSemyr) {
        this.takenSemyr = takenSemyr;
        updated = true;
    }

    public void updateInstructorName(String instructorName) {
        this.instructorName = instructorName;
        updated = true;
    }

    public void updateGrade(String grade) {
        this.grade = grade;
        updated = true;
    }

}
