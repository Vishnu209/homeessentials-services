package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "Testimonials")
@Data
public class Testimonial {

	@Id
	private String id;

	@Field("name")
	private String name;

	@Field("email")
	private String email;

	@Field("comment")
	private String comment;

	@Field("deleted_flag")
	private boolean deletedFlag;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastupdatedDate;
}
