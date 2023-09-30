package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.Testimonial;
import com.home.essentials.repository.TestimonialsRepository;
import com.home.essentials.service.TestimonialsService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestimonialsServiceImpl implements TestimonialsService {

	@Autowired
	private TestimonialsRepository testimonialsRepository;

	@Override
	public Testimonial save(Testimonial testimonials) {
		log.debug("Service request to save a testimonial ");
		testimonials.setCreatedDate(Instant.now());
		testimonials = testimonialsRepository.save(testimonials);
		return testimonials;
	}

	@Override
	public Testimonial getTestimonialsById(String testimonialsId) {
		log.debug("Service request to get a testimonial  by id ");
		return testimonialsRepository.getTestimonialById(testimonialsId)
				.orElseThrow(() -> new EntityNotFoundException(Testimonial.class, "testimonialsId", testimonialsId));
	}

	@Override
	public List<Testimonial> getAllTestimonials() {
		log.debug("Service request to get all testimonials");
		return testimonialsRepository.getAllTestimonials();
	}

	@Override
	public Testimonial updateTestimonials(Testimonial testimonials) {
		log.debug("Service request to update a testimonial by id ");
		testimonials.setLastupdatedDate(Instant.now());
		return testimonialsRepository.save(testimonials);
	}

	@Override
	public Testimonial deleteTestimonials(String testimonialsId) {
		log.debug("Service request to delete a testimonial with id  {}.", testimonialsId);
		Testimonial testimonial = testimonialsRepository.findById(testimonialsId)
				.orElseThrow(() -> new EntityNotFoundException(Testimonial.class, "testimonialsId", testimonialsId));
		testimonial.setDeletedFlag(true);
		return testimonialsRepository.save(testimonial);
	}

}
