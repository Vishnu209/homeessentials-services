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

import com.home.essentials.model.FilterInput;
import com.home.essentials.model.SortInput;
import com.home.essentials.repository.SortInputRepository;
import com.home.essentials.service.FilterInputService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "FilterInputController", description = "REST API for FilterInput ", tags = { "FilterInputController" })
@Slf4j
public class FilterInputController {

	@Autowired
	private FilterInputService filterInputService;  

	@Autowired
	private SortInputRepository sortInputRepo; 

	@PostMapping("/api/addFilterInput")
	@ApiOperation(value = " Create a filter input")
	public ResponseEntity<FilterInput> addFilterInput(@RequestBody @Valid FilterInput filterInput) throws Exception {
		FilterInput createdFilterInput = filterInputService.save(filterInput);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFilterInput);
	}

	@GetMapping("/api/getFilterInputById/{filterInputId}")
	@ApiOperation(value = " Get FilterInput  by Id")
	public ResponseEntity<FilterInput> getFilterInputById(@PathVariable String filterInputId) {
		log.info("Request to get FilterInput  by Id:{}", filterInputId);
		return ResponseEntity.ok(filterInputService.getFilterInputById(filterInputId));
	}

	@GetMapping("/api/getAllFilterInputs")
	@ApiOperation(value = " Get All FilterInputs with deleted flag as false")
	public ResponseEntity<List<FilterInput>> getAllFilterInputs() {
		log.info("Request to get All FilterInputs");
		return ResponseEntity.ok(filterInputService.getAllFilterInput());
	}

	@DeleteMapping("/api/deleteFilterInputById/{filterInputId}")
	@ApiOperation(value = " Delete FilterInput by Id")
	public ResponseEntity<FilterInput> deleteFilterInput(@PathVariable String filterInputId) {
		log.info("Request to delete FilterInput by Id:{}", filterInputId);
		return ResponseEntity.ok(filterInputService.deleteFilterInput(filterInputId));
	}

	@PutMapping("/api/updateFilterInputById")
	@ApiOperation(value = "Update FilterInput  By Id")
	public ResponseEntity<FilterInput> updateFilterInputById(@RequestBody FilterInput filterInput) {
		log.info("Request to update FilterInput with id::{}", filterInput.getId());
		return ResponseEntity.ok(filterInputService.updateFilterInput(filterInput));
	}
	
	@PostMapping("/api/sort/addSortInput")
	@ApiOperation(value = " Create a sort input")
	public ResponseEntity<SortInput> addSortInput(@RequestBody @Valid SortInput sortInput) throws Exception {
		SortInput createdSortInput = sortInputRepo.save(sortInput);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdSortInput);
	}
}
