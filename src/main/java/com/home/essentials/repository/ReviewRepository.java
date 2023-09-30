package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.RatingAndReview;

public interface ReviewRepository  extends MongoRepository<RatingAndReview ,String>{

	@Query(value = "{'deleted_flag' : false}")
	List<RatingAndReview> getAllReviews();
	
	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<RatingAndReview> findById(String id);
}
