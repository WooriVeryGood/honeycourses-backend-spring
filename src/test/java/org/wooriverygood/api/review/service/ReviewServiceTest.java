package org.wooriverygood.api.review.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@SpringBootTest
public class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;

    @MockBean
    private ReviewRepository reviewRepository;

    private final int REVIEW_COUNT = 10;

    List<Review> reviews = new ArrayList<>();

    Courses singleCourse= Courses.builder()
            .course_name("Gaoshu")
            .course_category("Zhuanye")
            .course_credit(5)
            .isYouguan(0)
            .kaikeYuanxi("Xinke")
            .build();

    @BeforeEach
    void setUpReviews() {
        for(int i = 0; i<REVIEW_COUNT; i++) {
            Review review = Review.builder()
                    .review_id(i)
                    .course(singleCourse)
                    .review_content("review" + i)
                    .review_title("review" + i)
                    .instructor_name("jiaoshou")
                    .taken_semyr("22-23")
                    .grade("60")
                    .author_email("author" + i)
                    .build();
            reviews.add(review);
        }
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 수업의 리뷰들을 불러온다.")
    void findAllReviewsByCourseId() {
        Mockito.when(reviewRepository.findAllByCourseId(anyInt()))
                .thenReturn(reviews);

        List<Review> responses = reviewService.findAllByCourseId(2);

        Assertions.assertThat(responses).hasSize(REVIEW_COUNT);
        Assertions.assertThat(responses.get(0).getReview_title()).isEqualTo("review0");

    }


}
