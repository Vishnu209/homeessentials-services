package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "contact us")
@Data
public class ContactUs {

	@Id
	private String id;

	@Field("username")
	private String username;

	@Field("email")
	private String email;

	@Field("message")
	private String message;

	@Field("subject")
	private String subject;
	
	@Field("contact_number")
	private String contactNumber;
	
	@Field("contact_us_status")
	private ContactUsStatus contactUsStatus;
	
	@Field("comments")
	private String comments;
	
	@Field("deleted_flag")
	private boolean deletedFlag =false;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastUpdatedDate ;

}
