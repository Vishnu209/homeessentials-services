package com.home.essentials.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.home.essentials.model.Product;

public interface ProductService {

	Product saveProduct(String productRequest, List<MultipartFile> productImage) throws Exception;

	Optional<Product> getProduct(String productId) throws Exception;

	Product updateProduct(String productRequest, List<MultipartFile> productImage) throws Exception;

	String deleteProduct(String productId) throws Exception;

}
