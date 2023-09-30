package com.home.essentials.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "filter inputs")
public class FilterInput {

	@Id
	private String id;

	@Field("price_inputs")
	private List<PriceInput> priceInputs;
	
	@Field("sort_inputs")
	private List<SortInput> sortInputs;

	@Field("currency")
	private String currency;

	@Field("min_order")
	private double minOrder;

	@Field("deleted_flag")
	private boolean deletedFlag;
}
