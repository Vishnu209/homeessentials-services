package com.home.essentials.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.home.essentials.model.Testimonial;
import com.home.essentials.service.TestimonialsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Testimonials Controller", description = "REST API for Testimonials ", tags = {
		"Testimonials Controller" })
@Slf4j
public class TestimonialsController {

	@Autowired
	private TestimonialsService testimonialsService;

	@PostMapping("/api/addTestimonial")
	@ApiOperation(value = " Create a Testimonial")
	public ResponseEntity<Testimonial> addTestimonial(@RequestBody @Valid Testimonial testimonial) {
		Testimonial createdTestimonial = testimonialsService.save(testimonial);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdTestimonial);
	}

	@GetMapping("/api/getTestimonialById/{testimonialId}")
	@ApiOperation(value = " Get Testimonial by Id")
	public ResponseEntity<Testimonial> getTestimonialById(@PathVariable String testimonialId) {
		log.info("Request to get testimonial  by Id:{}", testimonialId);
		return ResponseEntity.ok(testimonialsService.getTestimonialsById(testimonialId));
	}

	@GetMapping("/api/getAllTestimonials")
	@ApiOperation(value = " Get All Testimonials  excluding deleted ones")
	public ResponseEntity<List<Testimonial>> getAllTestomonials() {
		log.info("Request to get All testimonials");
		return ResponseEntity.ok(testimonialsService.getAllTestimonials());
	}

	@DeleteMapping("/api/deleteTestimonialById/{testimonialId}")
	@ApiOperation(value = " Delete Testimonial by Id")
	public ResponseEntity<Testimonial> deleteTestimonila(@PathVariable String testimonialId) {
		log.info("Request to delete testimonial by Id:{}", testimonialId);
		return ResponseEntity.ok(testimonialsService.deleteTestimonials(testimonialId));
	}

	@PutMapping("/api/updateTestimonialById")
	@ApiOperation(value = "Update Testimonial  By Id")
	public ResponseEntity<Testimonial> updateTestimonialById(@RequestBody Testimonial testimonial) {
		log.info("Request to update Testimonial with id::{}", testimonial.getId());
		return ResponseEntity.ok(testimonialsService.updateTestimonials(testimonial));
	}
}
