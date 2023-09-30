package com.home.essentials.model;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "Category")
@Getter
@Setter
public class Category {

	@Id
	private String id;

	@Field("category_name")
	private String categoryName;

	@Field("category_description")
	private String categoryDescription;

	@Field("category_code")
	private String categoryCode;

	@Field("created_by")
	private String createdBy;

	@Field("updated_by")
	private String updatedBy;
	
	@Field("product_count")
	private int productCount;

	@Field("deleted_flag")
	private boolean deletedFlag= false;

	@Field("created_date")
	private Instant createdDate = Instant.now();
	
	@Field("last_updated_date")
	private Instant lastupdatedDate ;	

}
