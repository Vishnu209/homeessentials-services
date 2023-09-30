package com.home.essentials.model;

import java.util.List;

import lombok.Data;

@Data
public class CategoryPriceFilter {

	private List<Category> categories;

	List<PriceRanges> priceInput;

}
