package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.Product;
import com.home.essentials.model.RatingAndReview;

public interface ReviewService {

	Product save(RatingAndReview review) throws Exception;

	RatingAndReview getReviewById(String reviewId);

	List<RatingAndReview> getAllReviews();

	Product updateReview(RatingAndReview review) throws Exception;

	Product deleteReview(String reviewId) throws Exception;
}
