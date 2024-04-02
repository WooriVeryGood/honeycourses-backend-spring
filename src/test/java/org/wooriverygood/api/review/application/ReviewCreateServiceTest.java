package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.NewReviewRequest;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReviewCreateServiceTest extends MockTest {

    @InjectMocks
    private ReviewCreateService reviewCreateService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private AuthInfo authInfo = AuthInfo.builder()
            .sub("22222-34534-123")
            .username("22222-34534-123")
            .build();

    private Course course = Course.builder()
            .id(1L)
            .name("Gaoshu")
            .category("Zhuanye")
            .credit(5)
            .isYouguan(0)
            .kaikeYuanxi("Xinke")
            .build();


    @Test
    @DisplayName("특정 강의의 리뷰를 작성한다.")
    void addReview() {
        NewReviewRequest request = NewReviewRequest.builder()
                .reviewTitle("Test Review from TestCode")
                .instructorName("Jiaoshou")
                .takenSemyr("1stSem")
                .reviewContent("Good!")
                .grade("100")
                .build();

        when(courseRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(course));

        reviewCreateService.addReview(authInfo, 1L, request);

        verify(reviewRepository).save(any(Review.class));
        verify(courseRepository).increaseReviewCount(course.getId());
    }

}