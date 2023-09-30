package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.State;

public interface StateRepository extends MongoRepository<State, String> {

	@Query(value = "{'deleted_flag' : false}")
	List<State> getAllStates();

	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<State> findById(String id);

}