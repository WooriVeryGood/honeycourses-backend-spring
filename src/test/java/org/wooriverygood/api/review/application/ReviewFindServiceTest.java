package org.wooriverygood.api.review.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.global.auth.AuthInfo;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.dto.ReviewsResponse;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.util.MockTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ReviewFindServiceTest extends MockTest {

    @InjectMocks
    private ReviewFindService reviewFindService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    private List<Review> reviews = new ArrayList<>();

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

    private final int REVIEW_COUNT = 10;


    @BeforeEach
    void setUp() {
        for(long i = 0; i < REVIEW_COUNT; i++) {
            Review review = Review.builder()
                    .id(i)
                    .course(course)
                    .reviewContent("review" + i)
                    .reviewTitle("review" + i)
                    .instructorName("jiaoshou")
                    .takenSemyr("22-23")
                    .grade("60")
                    .authorEmail("author" + i)
                    .reviewLikes(new ArrayList<>())
                    .updated(false)
                    .build();
            reviews.add(review);
        }
    }

    @Test
    @DisplayName("유효한 id를 통해 특정 수업의 리뷰들을 불러온다.")
    void findAllReviewsByCourseId() {
        when(reviewRepository.findAllByCourseId(any()))
                .thenReturn(reviews);
        when(reviewLikeRepository.existsByReviewAndUsername(any(Review.class), anyString()))
                .thenReturn(true);

        ReviewsResponse response = reviewFindService.findAllReviewsByCourseId(1L, authInfo);

        assertThat(response.getReviews()).hasSize(REVIEW_COUNT);
        assertThat(response.getReviews().get(0).getReviewTitle()).isEqualTo("review0");
    }

    @Test
    @DisplayName("사용자 본인이 작성한 리뷰들을 불러온다.")
    void findMyReviews() {
        when(reviewRepository.findByAuthorEmail(any(String.class)))
                .thenReturn(reviews);

        ReviewsResponse response = reviewFindService.findMyReviews(authInfo);

        assertThat(response.getReviews().get(0).isMine()).isEqualTo(true);
    }

}