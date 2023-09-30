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

import com.home.essentials.model.FAQ;
import com.home.essentials.service.FAQService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "FAQS Controller", description = "REST API for FAQs ", tags = { "FAQs Controller" })
@Slf4j
public class FAQController {

	@Autowired
	private FAQService faqService;

	@PostMapping("/api/addFAQ")
	@ApiOperation(value = " Create a FAQ")
	public ResponseEntity<FAQ> addFAQ(@RequestBody @Valid FAQ faq) {
		FAQ createdFaq = faqService.save(faq);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFaq);
	}

	@GetMapping("/api/getFAQById/{faqId}")
	@ApiOperation(value = " Get FAQ by Id")
	public ResponseEntity<FAQ> getFAQById(@PathVariable String faqId) {
		log.info("Request to get FAQ by Id:{}", faqId);
		return ResponseEntity.ok(faqService.getFAQById(faqId));
	}

	@GetMapping("/api/getAllFAQs")
	@ApiOperation(value = " Get All FAQs excluding deleted ones")
	public ResponseEntity<List<FAQ>> getAllFAQs() {
		log.info("Request to get All FAQs");
		return ResponseEntity.ok(faqService.getAllFAQs());
	}

	@DeleteMapping("/api/deleteFAQById/{faqId}")
	@ApiOperation(value = " Delete FAQ by Id")
	public ResponseEntity<FAQ> deleteFAQ(@PathVariable String faqId) {
		log.info("Request to delete FAQ by Id:{}", faqId);
		return ResponseEntity.ok(faqService.deleteFAQ(faqId));
	}

	@PutMapping("/api/updateFAQById")
	@ApiOperation(value = "Update FAQ  By Id")
	public ResponseEntity<FAQ> updateFAQById(@RequestBody FAQ faq) {
		log.info("Request to update FAQ with id::{}", faq.getId());
		return ResponseEntity.ok(faqService.updateFAQ(faq));
	}

}
