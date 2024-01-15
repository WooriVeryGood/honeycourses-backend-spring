package org.wooriverygood.api.course.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "courses")
public class Courses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int course_id;

    @Column(name = "course_category", nullable = false)
    private String course_category;

    @Column(name = "course_credit", nullable = false)
    private int course_credit;

    @Column(name = "course_name", nullable = false)
    private String course_name;

    @Column(name = "isYouguan", nullable = false)
    private int isYouguan;

    @Column(name = "kaikeYuanxi", nullable = false)
    private String kaikeYuanxi;

    @Builder
    public Courses(int course_id, String course_name, String course_category, int course_credit, int isYouguan, String kaikeYuanxi) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_category = course_category;
        this.course_credit = course_credit;
        this.isYouguan = isYouguan;
        this.kaikeYuanxi = kaikeYuanxi;
    }



}
