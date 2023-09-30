package com.home.essentials.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.Category;
import com.home.essentials.payload.FailureResponse;
import com.home.essentials.repository.CategoryRepository;
import com.home.essentials.repository.ProductRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.impl.CategoryServiceImpl;
import com.home.essentials.service.impl.InventoryServiceImpl;
import com.home.essentials.service.impl.ProductServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Category Controller", description = "REST API for ProductCategories ", tags = { "Category Controller" })
@Slf4j
public class CategoryController {

	@Autowired
	private ProductServiceImpl productServiceImpl;

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;

	@Autowired
	private InventoryServiceImpl inventoryServiceImpl;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/api/getCategory")
	@ApiOperation(value = "Get Category By Id", response = Category.class)
	public ResponseEntity<?> getCategory(@RequestParam("id") String id) throws Exception {
		Optional<Category> category = categoryServiceImpl.getCategory(id);
		if (categoryRepository.existsById(id)) {

			return new ResponseEntity<>(category, HttpStatus.OK);
		} else
			return new ResponseEntity<>(new FailureResponse("CategoryID  doesnot exists in the database"),
					HttpStatus.NOT_FOUND);
	}

	@GetMapping("/api/getAllCategories")
	@ApiOperation(value = "Get All Categories")
	public ResponseEntity<?> getAllCategory() throws Exception {
		List<Category> categories = categoryServiceImpl.getAllCategory();

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@GetMapping("/api/home/getAllCategories")
	@ApiOperation(value = "Get All Categories for home page")
	public ResponseEntity<?> getAllCategoryForHomePage() throws Exception {
		List<Category> categories = categoryServiceImpl.getAllCategory();

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}
}
