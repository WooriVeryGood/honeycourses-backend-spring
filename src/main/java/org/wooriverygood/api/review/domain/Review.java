package org.wooriverygood.api.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.wooriverygood.api.course.domain.Courses;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Courses course;

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

    @Builder
    public Review(Long id, Courses course, String reviewContent, String reviewTitle, String instructorName, String takenSemyr, String grade, String authorEmail) {
        this.id = id;
        this.course = course;
        this.reviewContent = reviewContent;
        this.reviewTitle = reviewTitle;
        this.instructorName = instructorName;
        this.takenSemyr = takenSemyr;
        this.grade = grade;
        this.authorEmail = authorEmail;
    }







}
