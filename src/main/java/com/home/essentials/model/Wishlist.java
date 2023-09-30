package com.home.essentials.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Wishlist")
public class Wishlist {


	private String productId;

	private String productName;
	 
	private String ImageId;
	
	private List<String> productImageUrl;
	
	private String productCost;
	
}
