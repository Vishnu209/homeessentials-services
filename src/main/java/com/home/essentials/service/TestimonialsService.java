package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.Testimonial;

public interface TestimonialsService {

	Testimonial save(Testimonial testimonials);

	Testimonial getTestimonialsById(String testimonialsId);

	List<Testimonial> getAllTestimonials();

	Testimonial updateTestimonials(Testimonial testimonials);

	Testimonial deleteTestimonials(String testimonialsId);
}
