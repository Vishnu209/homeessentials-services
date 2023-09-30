package com.home.essentials.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "state")
@Data
public class State {

	@Id
	private String id;
	
	@Field("state_name")
	private String stateName;
	
	@Field("country_name")
	private String countryName;
	
	@Field("delivery_charge")
	private BigDecimal deliveryCharge ;
	
	@Field("deleted_flag")
	private boolean deletedFlag =false;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastupdatedDate ;

}