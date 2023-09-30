package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "FAQ")
@Data
public class FAQ {

	@Id
	private String id;

	@Field("question")
	private String question;

	@Field("answer")
	private String answer;

	@Field("deleted_flag")
	private boolean deletedFlag;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastupdatedDate;
}