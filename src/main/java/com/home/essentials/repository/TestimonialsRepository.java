package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.Testimonial;

public interface TestimonialsRepository  extends MongoRepository<Testimonial, String>{

	@Query(value = "{'deleted_flag' : false}")
	List<Testimonial> getAllTestimonials();
	
	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<Testimonial> getTestimonialById(String testimonialId);
}
