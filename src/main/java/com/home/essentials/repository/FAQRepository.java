package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.FAQ;

public interface FAQRepository extends MongoRepository<FAQ, String> {

	@Query(value = "{'deleted_flag' : false}")
	List<FAQ> getAllFAQs();

	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<FAQ> getFAQById(String FAQId);

}
