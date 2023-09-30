package com.home.essentials.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.home.essentials.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	List<Product> findAllByCategoryId(String categoryId);

	List<Product> findByProductName(String productName);
	
}