package org.wooriverygood.api.course.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_category", nullable = false)
    private String category;

    @Column(name = "course_credit", nullable = false)
    private double credit;

    @Column(name = "course_name", nullable = false)
    private String name;

    @Column(name = "isYouguan", nullable = false)
    private int isYouguan;

    @Column(name = "kaikeYuanxi", nullable = false)
    private String kaikeYuanxi;

    @Column(name = "review_count", nullable = false)
    @ColumnDefault("0")
    private int reviewCount;

    @Builder
    public Course(Long id, String name, String category, double credit, int isYouguan, String kaikeYuanxi, int reviewCount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.credit = credit;
        this.isYouguan = isYouguan;
        this.kaikeYuanxi = kaikeYuanxi;
        this.reviewCount = reviewCount;
    }

}
