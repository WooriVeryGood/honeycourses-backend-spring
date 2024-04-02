package org.wooriverygood.api.review.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewsResponse {

    private final List<ReviewResponse> reviews;


    public ReviewsResponse(List<ReviewResponse> reviews) {
        this.reviews = reviews;
    }

}
