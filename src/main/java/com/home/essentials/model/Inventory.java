package com.home.essentials.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Document(collection = "inventory")
@Data
public class Inventory {

	@Id
	private String id;

	@Field("product_id")
	private String productId;
	
	@Field("product_name")
	private String productName;

	@Field("available_stock")
	private int available_stock;
	
	@Field("sold")
	private int sold;

	@Field("deleted_flag")
	private boolean deletedFlag=false;

	@Field("created_date")
	private Instant createdDate = Instant.now();
	
	@Field("last_updated_date")
	private Instant lastupdatedDate ;	

}
