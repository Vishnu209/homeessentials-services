package com.home.essentials.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.home.essentials.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

}