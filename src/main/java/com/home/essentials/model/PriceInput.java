package com.home.essentials.model;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class PriceInput {

	@Field("min_value")
	private int minValue;
	
	@Field("max_value")
	private int maxValue;
	
	@Field("price_range_name")
	private String priceRangeName;
	
	@Field("code")
	private String code;   // example 0-100, 100-500 etc.
	
	@Field("description_for_price_range")
	private String descriptionForPriceRange;
}
