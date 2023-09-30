package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.FilterInput;

public interface FilterInputRepository extends MongoRepository<FilterInput, String>{

	@Query(value = "{'deleted_flag' : false}")
	List<FilterInput> getAllFilterInput();

	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<FilterInput> findById(String id);
}
