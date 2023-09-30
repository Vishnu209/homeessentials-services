package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.ContactUs;

public interface ContactUsRepository extends MongoRepository<ContactUs,String>{

	@Query(value = "{'deleted_flag' : false}")
	List<ContactUs> getAllContactUsPages(Sort sort);
	
	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<ContactUs> findById(String id);
}
