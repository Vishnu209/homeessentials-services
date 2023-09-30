package com.home.essentials.model;

import java.util.List;

import lombok.Data;

@Data
public class ShoppingCartEntry {

	private String productId;
	private String imageId;
	private List<String> productImageUrl;
	private String productName;
	private String productCost;
	private String productCategory;
	private int quantity;
	private double  productTotalPrice;
	private String colour;
	private String size;
	private String packagingQuantities;

	
}
