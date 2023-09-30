package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.SortInput;

public interface SortInputRepository extends MongoRepository<SortInput, String>{

	@Query(value = "{'deleted_flag' : false}")
	List<SortInput> getAllFilterInput();

	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<SortInput> findById(String id);
	
	@Query(value = "{'deleted_flag' : false, 'code':?0}")
	Optional<SortInput> findByCode(String code);
}
