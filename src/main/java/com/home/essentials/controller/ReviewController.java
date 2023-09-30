package com.home.essentials.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.Product;
import com.home.essentials.model.RatingAndReview;
import com.home.essentials.service.ReviewService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Review Controller", description = "REST API for Product Reviews ", tags = { "Review Controller" })
@Slf4j
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;
	
	@PostMapping("/api/addReview")
	@ApiOperation(value = " Create a Review")
	public ResponseEntity<Product> addReview(@RequestBody @Valid RatingAndReview review) throws Exception {
		Product createdProductWithReview = reviewService.save(review);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdProductWithReview);
	}
	@GetMapping("/api/getReviewById/{reviewId}")
	@ApiOperation(value = " Get Review  by Id")
	public ResponseEntity<RatingAndReview> getReviewById(@PathVariable String reviewId) {
		log.info("Request to get review  by Id:{}", reviewId);
		return ResponseEntity.ok(reviewService.getReviewById(reviewId));
	}
	
	@GetMapping("/api/getAllReviews")
	@ApiOperation(value = " Get All Reviews  with deleted flag as false")
	public ResponseEntity<List<RatingAndReview>> getAllReviews() {
		log.info("Request to get All Reviews");
		return ResponseEntity.ok(reviewService.getAllReviews());
	}

	@DeleteMapping("/api/deleteReviewById/{reviewId}")
	@ApiOperation(value = " Delete Review by Id")
	public ResponseEntity<Product> deleteReview(@PathVariable String reviewId) throws Exception {
		log.info("Request to delete Review by Id:{}", reviewId);
		return ResponseEntity.ok(reviewService.deleteReview(reviewId));
	}

	@PutMapping("/api/updateReviewById")
	@ApiOperation(value = "Update review By Id")
	public ResponseEntity<Product> updateReviewIdById(@RequestBody RatingAndReview review) throws Exception {
		log.info("Request to update review with id::{}", review.getId());
		return ResponseEntity.ok(reviewService.updateReview(review));
	}
}
