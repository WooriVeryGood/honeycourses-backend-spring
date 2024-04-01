package org.wooriverygood.api.review.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wooriverygood.api.global.error.exception.AuthorizationException;
import org.wooriverygood.api.course.domain.Course;
import org.wooriverygood.api.course.repository.CourseRepository;
import org.wooriverygood.api.review.domain.Review;
import org.wooriverygood.api.review.domain.ReviewLike;
import org.wooriverygood.api.review.dto.*;
import org.wooriverygood.api.review.repository.ReviewLikeRepository;
import org.wooriverygood.api.review.repository.ReviewRepository;
import org.wooriverygood.api.global.auth.AuthInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;
    private final int REVIEW_COUNT = 10;

    List<Review> reviews = new ArrayList<>();

    Course singleCourse= Course.builder()
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
                    .reviewLikes(new ArrayList<>())
                    .updated(false)
                    .build();
            reviews.add(review);
        }
    }

    Review singleReview = Review.builder()
            .id(1L)
            .course(singleCourse)
            .reviewContent("test review")
            .reviewTitle("test review title")
            .instructorName("jiaoshou")
            .takenSemyr("22-23")
            .grade("100")
            .authorEmail(authInfo.getUsername())
            .reviewLikes(new ArrayList<>())
            .updated(false)
            .createdAt(LocalDateTime.of(2022, 6, 13, 12, 00))
            .build();


    @Test
    @DisplayName("특정 리뷰의 좋아요를 1 올린다.")
    void likeReview_up() {
        Mockito.when(reviewRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleReview));

        ReviewLikeResponse response = reviewService.likeReview(singleReview.getId(), authInfo);

        Assertions.assertThat(response.getLike_count()).isEqualTo(singleReview.getLikeCount() + 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 리뷰의 좋아요를 1 내린다.")
    void likeReview_down() {
        ReviewLike reviewLike = ReviewLike.builder()
                .id(3L)
                .review(singleReview)
                .username(authInfo.getUsername())
                .build();

        Mockito.when(reviewRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleReview));
        Mockito.when(reviewLikeRepository.findByReviewAndUsername(any(Review.class), any(String.class)))
                .thenReturn(Optional.ofNullable(reviewLike));

        ReviewLikeResponse response = reviewService.likeReview(singleReview.getId(), authInfo);

        Assertions.assertThat(response.getLike_count()).isEqualTo(singleReview.getLikeCount() - 1);
        Assertions.assertThat(response.isLiked()).isEqualTo(false);
    }

    @Test
    @DisplayName("권한이 있는 리뷰를 수정한다.")
    void updateReview() {
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .review_title("new title")
                .review_content("new content")
                .instructor_name("jiaoshou")
                .taken_semyr("18-19")
                .grade("100")
                .build();

        Mockito.when(reviewRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(singleReview));

        ReviewUpdateResponse response = reviewService.updateReview(singleReview.getId(), request, authInfo);

        Assertions.assertThat(response.getReview_id()).isEqualTo(singleReview.getId());
        Assertions.assertThat(singleReview.isUpdated()).isEqualTo(true);
    }

//    @Test
//    @DisplayName("권한이 없는 리뷰는 수정이 불가능하다.")
//    void updateReview_noAuth() {
//        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
//                .review_title("new title")
//                .review_content("new content")
//                .build();
//
//        Mockito.when(reviewRepository.findById(any(Long.class)))
//                .thenReturn(Optional.ofNullable(noAuthReview));
//
//        Assertions.assertThatThrownBy(() -> reviewService.updateReview(noAuthReview.getId(), request, authInfo))
//                .isInstanceOf(AuthorizationException.class);
//    }

    }
