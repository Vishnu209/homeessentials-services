package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.exception.ItemNotFoundException;
import com.home.essentials.model.Category;
import com.home.essentials.model.User;
import com.home.essentials.repository.CategoryRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Category registerCategory(Category category) throws Exception {
		category.setDeletedFlag(false);
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loggedUser =userRepository.findByUsername(user);
		category.setCreatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
		category = categoryRepository.save(category);
		return category;
	}

	@Override
	public Optional<Category> getCategory(String categoryId) throws Exception {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isPresent()) {
			Category categoryInDb = category.get();
			if (!categoryInDb.isDeletedFlag()) // checking if deletedFlag is false
				return category;
			else
				throw new ItemNotFoundException(categoryId);
		}
		return null;
	}

	public List<Category> getAllCategory() throws Exception {
		List<Category> allCategories = categoryRepository.findAll();
		List<Category> categories = new ArrayList<>();
		allCategories.stream().forEach(x ->{
			if(!x.isDeletedFlag())
				categories.add(x);
			else
				allCategories.retainAll(allCategories);
		});				
		return categories;
	}

	@Override
	public Category updateCategory(Category category) throws Exception {
		Optional<Category> categoryInDB = categoryRepository.findById(category.getId());
		if (categoryInDB.isPresent()) {
			Category editCategory = categoryInDB.get();
			//categoryRepository.deleteById(editCategory.getId());
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			category.setDeletedFlag(false);
			category.setCreatedDate(editCategory.getCreatedDate());
			category.setLastupdatedDate(Instant.now());
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			Optional<User> loggedUser =userRepository.findByUsername(user);
			category.setUpdatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());
			category = categoryRepository.save(category);
			return category;
		} else
			throw new ItemNotFoundException(category.getId());
	}

	@Override
	public String deleteCategory(String categoryId) throws Exception {
		Optional<Category> categoryInDb = categoryRepository.findById(categoryId);
		if (categoryInDb.isPresent()) {
			Category categoryToDelete = categoryInDb.get();
			categoryToDelete.setDeletedFlag(true);
			categoryToDelete = categoryRepository.save(categoryToDelete);
			return "Catgeory Deleted";
		} else
			return "Category doesnot exist";

	}
}
