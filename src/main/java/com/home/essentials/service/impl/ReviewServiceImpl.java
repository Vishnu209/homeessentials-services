package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.Product;
import com.home.essentials.model.RatingAndReview;
import com.home.essentials.model.RatingAndReviewResponse;
import com.home.essentials.model.User;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.repository.ReviewRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product save(RatingAndReview review) throws Exception {
		log.debug("Service request to save a Review ");
		if (review.getEmail() == null || review.getEmail().isEmpty()) {
			throw new Exception("email id is required");
		}

		review.setCreatedDate(Instant.now());
		List<RatingAndReviewResponse> replies = new ArrayList<RatingAndReviewResponse>();
		review.setReplies(replies);
		Optional<Product> productInDb = productRepository.findById(review.getProductId());
		Product product = null;
		if (productInDb.isPresent()) {
			product = productInDb.get();
		}
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loggedUser = userRepository.findByUsername(user);
		review.setCreatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
		review = reviewRepository.save(review);

		// setting the review to product domain.

		List<RatingAndReview> reviews = product.getReviews();
		if (!reviews.isEmpty() || reviews != null) {
			reviews.add(review);
			product.setReviews(reviews);
		}
		product = productRepository.save(product);

		return product;
	}

	@Override
	public RatingAndReview getReviewById(String reviewId) {
		log.debug("Service request to get a review by id");
		return reviewRepository.findById(reviewId)
				.orElseThrow(() -> new EntityNotFoundException(RatingAndReview.class, "reviewId", reviewId));
	}

	@Override
	public List<RatingAndReview> getAllReviews() {
		log.debug("Service request to get the list of reviews");
		return reviewRepository.getAllReviews();
	}

	@Override
	public Product updateReview(RatingAndReview review) {
		log.debug("Service request to update  a review  by id");
		review.setLastupdatedDate(Instant.now());
		Product product = null;
		Optional<Product> productInDb = productRepository.findById(review.getProductId());
		if (productInDb.isPresent()) {
			product = productInDb.get();
			// set the rating to product domain.
			// product.setRating(review.getRating());
		}
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loggedUser = userRepository.findByUsername(user);
		review.setUpdatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
		review = reviewRepository.save(review);

		String id = review.getId();
		// setting the review to product domain.
		List<RatingAndReview> reviews = product.getReviews();
		if (!reviews.isEmpty() || reviews != null) {
			reviews.removeIf(x -> x.getId().equals(id));
			reviews.add(review);
			product.setReviews(reviews);
		}
		product = productRepository.save(product);
		return product;
	}

	@Override
	public Product deleteReview(String reviewId) throws Exception {
		log.debug("Service request to delete a review  with id {}.", reviewId);
		RatingAndReview review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new EntityNotFoundException(RatingAndReview.class, "reviewId", reviewId));
		review.setDeletedFlag(true);
		review = reviewRepository.save(review);
		Optional<Product> productWithReview = productRepository.findById(review.getProductId());
		Product product = null;
		if (productWithReview.isPresent()) {
			product = productWithReview.get();
		}
		
		// setting the review to product domain.
		List<RatingAndReview> reviews = product.getReviews();
		if (!reviews.isEmpty() || reviews != null) {
			reviews.removeIf(x -> x.getId().equals(reviewId));
			product.setReviews(reviews);
		}
		product = productRepository.save(product);
			
		return product;
	}
}
