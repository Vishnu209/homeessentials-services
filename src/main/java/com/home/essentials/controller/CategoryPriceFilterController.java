package com.home.essentials.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.CategoryPriceFilter;
import com.home.essentials.model.Product;
import com.home.essentials.service.CategoryPriceFilterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Category & Price Filter Controller", description = "Filter for Category And Price Ranges", tags = { "Category & Price Filter Controller" })
public class CategoryPriceFilterController {
	
	@Autowired
	private CategoryPriceFilterService categoryPriceFilterService;
	
	@PostMapping("/api/addFiltersWithPriceRange")
	@ApiOperation(value = "Returns List of Products based on CategoryId & Price . If CategoryId list is empty and prices are selected, returns all products with price range and vice versa. if both selected, returns products based on category & prices")
	public ResponseEntity<List<Product>>  getFilters(@RequestBody CategoryPriceFilter categoryPriceFilter){
		
		List<Product> products = categoryPriceFilterService.filterByCategoryAndPrice(categoryPriceFilter);
		return new ResponseEntity<>(products, HttpStatus.OK);
}
}
