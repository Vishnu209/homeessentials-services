package com.home.essentials.model;

import java.time.Instant;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "Coupons")
@Data
public class Coupon {

	@Id
	 private String id;
	
	@Field("coupon_code")
	private String couponCode;
	
	@Field("coupon_discount")
	private String couponDiscount;
	
	@Field("coupon_description")
	private String couponDescription;
	
	@Field("coupon_category")
	private String couponCategory;
	
	@Field("is_coupon_valid")
	private boolean isCouponValid;
	
	@Field("start_date")
	private Date startDate;
	
	@Field("end_date")
	private Date endDate;
	
	@Field("created_by")
	private String createdBy;

	@Field("updated_by")
	private String updatedBy;

	@Field("deleted_flag")
	private boolean deletedFlag= false;

	@Field("created_date")
	private Instant createdDate = Instant.now();
	
	@Field("last_updated_date")
	private Instant lastupdatedDate ;
	
}
