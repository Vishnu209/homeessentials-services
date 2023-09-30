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

import com.home.essentials.model.ContactUs;
import com.home.essentials.service.ContactUsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "ContactUs Controller", description = "REST API for ContactUs ", tags = { "ContactUs Controller" })
@Slf4j
public class ContactUsController {

	@Autowired
	private ContactUsService contactUsService;

	@PostMapping("/api/addContactUsPage")
	@ApiOperation(value = " Create a Contact Us Page")
	public ResponseEntity<ContactUs> addContactUs(@RequestBody @Valid ContactUs contactUs) {
		ContactUs createdContactUs = contactUsService.save(contactUs);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdContactUs);
	}

	@GetMapping("/api/getContactUsPageById/{contactUsId}")
	@ApiOperation(value = " Get ContactUs Page by Id")
	public ResponseEntity<ContactUs> getContactUsById(@PathVariable String contactUsId) {
		log.info("Request to get ContactUs Page  by Id:{}", contactUsId);
		return ResponseEntity.ok(contactUsService.getContactUsById(contactUsId));
	}

	@GetMapping("/api/getAllContactUsPages")
	@ApiOperation(value = " Get All ContactUs Pages with deleted flag as false")
	public ResponseEntity<List<ContactUs>> GetAllContactUPages() {
		log.info("Request to get contactUs pages");
		return ResponseEntity.ok(contactUsService.getAllContactUs());
	}

	@DeleteMapping("/api/deleteContactUsPageById/{contactUsId}")
	@ApiOperation(value = " Delete ContactUs Page by Id")
	public ResponseEntity<ContactUs> deleteContactUsPage(@PathVariable String contactUsId) {
		log.info("Request to delete contactUs Page by Id:{}", contactUsId);
		return ResponseEntity.ok(contactUsService.deleteContactUs(contactUsId));
	}

	@PutMapping("/api/updateContactUsPageById")
	@ApiOperation(value = "Update ContactUs Page By Id")
	public ResponseEntity<ContactUs> updateContactUsById(@RequestBody ContactUs contactUs) {
		log.info("Request to update ContactUs Page with id::{}", contactUs.getId());
		return ResponseEntity.ok(contactUsService.updateContactUs(contactUs));
	}
}
