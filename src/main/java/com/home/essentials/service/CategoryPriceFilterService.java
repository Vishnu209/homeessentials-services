package com.home.essentials.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.model.Category;
import com.home.essentials.model.CategoryPriceFilter;
import com.home.essentials.model.PriceRanges;
import com.home.essentials.model.Product;
import com.home.essentials.repository.CategoryRepository;
import com.home.essentials.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CategoryPriceFilterService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	public List<Product> filterByCategoryAndPrice(CategoryPriceFilter categoryPriceFilter) {

		List<Product> productsWithFilters = new ArrayList<Product>();
		if (categoryPriceFilter.getCategories() != null && !categoryPriceFilter.getCategories().isEmpty()) {
			log.info("category exists !!!!!!!!");

			List<Category> categories = categoryPriceFilter.getCategories();
			categories.stream().forEach(category -> {
				List<Product> products = productRepository.findAllByCategoryId(category.getId());

				products.stream().forEach(product -> {

					if (categoryPriceFilter.getPriceInput() != null && !categoryPriceFilter.getPriceInput().isEmpty()) {
						log.info("price range present!!");
						List<PriceRanges> priceInputs = categoryPriceFilter.getPriceInput();

						priceInputs.stream().forEach(price -> {
							// checking for offer. if offer is null or empty, then productDiscountedCost
							// will be 0 when creating products
							if (product.getProductDiscountedCost() != 0) {
								log.info("Offer is present!!! So filtering based on productDiscountedCost field..");
								if (product.getProductDiscountedCost() > price.getMinPrice()
										&& product.getProductDiscountedCost() < price.getMaxPrice()) {
									productsWithFilters.add(product);
								}
							} else {
								log.info("Offer not present. Filtering based on productCost");
								if (Integer.parseInt(product.getProductCost()) > price.getMinPrice()
										&& Integer.parseInt(product.getProductCost()) < price.getMaxPrice()) {
									productsWithFilters.add(product);
								}
							}
						});
					}

				});
			});

		} else {
			log.info("catgeory is not present.. So fetching all products with price range....");
			List<Product> allProducts = productRepository.findAll();

			allProducts.stream().forEach(prod -> {

				if (categoryPriceFilter.getPriceInput() != null && !categoryPriceFilter.getPriceInput().isEmpty()) {
					log.info("price range present!!");
					List<PriceRanges> priceInputs = categoryPriceFilter.getPriceInput();
					priceInputs.stream().forEach(price -> {

						if (prod.getProductDiscountedCost() != 0) {
							log.info("Offer is present!!! So filtering based on productDiscountedCost field..");
							if (prod.getProductDiscountedCost() > price.getMinPrice()
									&& prod.getProductDiscountedCost() < price.getMaxPrice()) {
								productsWithFilters.add(prod);
							}
						} else {
							log.info("Offer not present. Filtering based on productCost");
							if (Integer.parseInt(prod.getProductCost()) > price.getMinPrice()
									&& Integer.parseInt(prod.getProductCost()) < price.getMaxPrice()) {
								productsWithFilters.add(prod);
							}
						}
					});
				}

			});
		}

		if (categoryPriceFilter.getPriceInput() == null || categoryPriceFilter.getPriceInput().isEmpty()) {
			log.info("price range input is not present. Returning products based on the category filters.....");
			List<Category> categories = categoryPriceFilter.getCategories();
			categories.stream().forEach(category -> {
				List<Product> products = productRepository.findAllByCategoryId(category.getId());
				products.stream().forEach(product -> {
					if (product.getCategoryId().equals(category.getId())) {
						productsWithFilters.add(product);
					}
				});
			});
		}

		return productsWithFilters;

	}
}
