package com.home.essentials.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "rating and review")
public class RatingAndReview {

	@Id
	private String id;
	
	@Field("replies")
	private List<RatingAndReviewResponse> replies;
	
	@Field("stars")
	private Star star;
	
	@Field("email")
	private String email;
	
	@Field("username")
	private String username;
	
	@Field("user_id")
	private String userId;
	
	@Field("comment")
	private String reviewComment;
	
	//@Field("rating")
	//private double rating;
	
	@Field("product_id")
	private String productId;
	
	@Field("product_name")
	private String productName;
	
	@Field("created_by")
	private String createdBy;

	@Field("updated_by")
	private String updatedBy;

	@Field("deleted_flag")
	private boolean deletedFlag= false;

	@Field("created_date")
	private Instant createdDate;
	
	@Field("last_updated_date")
	private Instant lastupdatedDate ;	

}
