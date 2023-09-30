package com.home.essentials.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class RatingAndReviewResponse {

	
	@Id
	private String id;
	
	
	@Field("email")
	private String email;
	
	@Field("username")
	private String username;
	
	@Field("userId")
	private String userId;
	
	@Field("comment")
	private String reviewComment;
	
	//@Field("created_by")
	//private String createdBy;

	//@Field("updated_by")
	//private String updatedBy;

	@Field("deleted_flag")
	private boolean deletedFlag= false;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastupdatedDate ;
}
