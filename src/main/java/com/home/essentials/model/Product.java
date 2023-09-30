package com.home.essentials.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Document(collection = "Products")
@Data
public class Product {

	@Id
	private String id;

	@Field("product_name")
	private String productName;

	@Field("product_description")
	private String productDescription;

	@Field("image_url")
	private String imageUrl; // this field is necessary for the FE for image uploading

	@Field("product_image_url")
	private List<String> productImageUrl;

	@Field("reviews")
	private List<RatingAndReview> reviews;

	@Field("image_id")
	private String imageId;

	@Field("price")
	private String productCost;

	@Field("quantity")
	private String quantity;

	@Field("available_stock")
	private int available_stock;

	@Field("sold")
	private int sold;

	@Field("offer")
	private String offer;

	@Field("product_discounted_cost")
	private int productDiscountedCost;

	@Field("categoryId")
	private String categoryId;

	@Field("category_name")
	private String categoryName;

	@Field("colour")
	private List<String> colour;

	@Field("size")
	private List<String> size;

	@Field("packaging_quantities")
	private List<String> packagingQuantities;

	@Field("created_by")
	private String createdBy;

	@Field("updated_by")
	private String updatedBy;

	@Field("deleted_flag")
	private boolean deletedFlag = false;

	@Field("created_date")
	private Instant createdDate;

	@Field("last_updated_date")
	private Instant lastupdatedDate;

}
