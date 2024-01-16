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
    private int review_id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Courses course;

    @Column(name = "review_content", length = 1000)
    private String review_content;

    @Column(name = "review_title", length = 45)
    private String review_title;

    @Column(name = "instructor_name", length = 45, nullable = false)
    private String instructor_name;

    @Column(name = "taken_semyr", length = 45, nullable = false)
    private String taken_semyr;

    @Column(name = "grade", length = 45, nullable = false)
    private String grade;

    @Column(name = "author_email", length = 300)
    private String author_email;

    @Builder
    public Review(int review_id, Courses course, String review_content, String review_title, String instructor_name, String taken_semyr, String grade, String author_email) {
        this.review_id = review_id;
        this.course = course;
        this.review_content = review_content;
        this.review_title = review_title;
        this.instructor_name = instructor_name;
        this.taken_semyr = taken_semyr;
        this.grade = grade;
        this.author_email = author_email;
    }







}
