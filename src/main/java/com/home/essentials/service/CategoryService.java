package com.home.essentials.service;

import java.util.Optional;

import com.home.essentials.model.Category;

public interface CategoryService  {
	
	Category registerCategory(Category category) throws Exception;

    Optional<Category> getCategory(String categoryId) throws Exception;

    Category updateCategory(Category category) throws Exception;

    String deleteCategory(String categoryId) throws Exception;
}
