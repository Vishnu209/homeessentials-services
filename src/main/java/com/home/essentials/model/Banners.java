package com.home.essentials.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "banners")
public class Banners {

	@Id
	private String id;
	
	@Field("image_name")
	private String imageName;
	
	@Field("location")
	private String location;
	
	@Field("banner_S3_url")
	private  List<String> bannerS3Url;
	
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
