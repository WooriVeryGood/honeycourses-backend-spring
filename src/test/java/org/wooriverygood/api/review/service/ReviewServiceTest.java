package org.wooriverygood.api.review.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wooriverygood.api.course.domain.Courses;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.dto.NewReviewResponse;
import org.wooriverygood.api.review.dto.ReviewResponse;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.support.AuthInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseRepository courseRepository;

    private final int REVIEW_COUNT = 10;

    List<Review> reviews = new ArrayList<>();

    Courses singleCourse= Courses.builder()
            .id(1L)
            .course_name("Gaoshu")
            .course_category("Zhuanye")
            .course_credit(5)
            .isYouguan(0)
            .kaikeYuanxi("Xinke")
            .build();

    AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    @BeforeEach
    void setUpReviews() {
        for(int i = 0; i<REVIEW_COUNT; i++) {
            Review review = Review.builder()
                    .id((long)i)
                    .course(singleCourse)
                    .reviewContent("review" + i)
                    .reviewTitle("review" + i)
                    .instructorName("jiaoshou")
                    .takenSemyr("22-23")
                    .grade("60")
                    .authorEmail("author" + i)
                    .build();
            reviews.add(review);
        }
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 수업의 리뷰들을 불러온다.")
    void findAllReviewsByCourseId() {
        Mockito.when(reviewRepository.findAllByCourseId(any()))
                .thenReturn(reviews);

        List<ReviewResponse> responses = reviewService.findAllReviewsByCourseId(2L);

        Assertions.assertThat(responses).hasSize(REVIEW_COUNT);
        Assertions.assertThat(responses.get(0).getReview_title()).isEqualTo("review0");

    }

    @Test
    @DisplayName("특정 강의의 리뷰를 작성한다.")
    void addReview() {
        NewReviewRequest newReviewRequest = NewReviewRequest.builder()
                .review_title("Test Review from TestCode")
                .instructor_name("Jiaoshou")
                .taken_semyr("1stSem")
                .review_content("Good!")
                .grade("100")
                .build();

        Mockito.when(reviewRepository.save(any(Review.class)))
                .thenReturn(Review.builder()
                        .authorEmail(authInfo.getUsername())
                        .reviewTitle(newReviewRequest.getReview_title())
                        .instructorName(newReviewRequest.getInstructor_name())
                        .takenSemyr(newReviewRequest.getTaken_semyr())
                        .reviewContent(newReviewRequest.getReview_content())
                        .grade(newReviewRequest.getGrade())
                        .build());

        Mockito.when(courseRepository.findById(any()))
                .thenReturn(Optional.ofNullable(singleCourse));

        NewReviewResponse response = reviewService.addReview(authInfo, 1L, newReviewRequest);

        Assertions.assertThat(response.getAuthor_email()).isEqualTo(authInfo.getUsername());
        Assertions.assertThat(response.getReview_content()).isEqualTo(newReviewRequest.getReview_content());
    }


}
