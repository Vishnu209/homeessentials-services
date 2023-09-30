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

import com.home.essentials.model.State;
import com.home.essentials.service.StateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "State Controller", description = "REST API for State ", tags = { "State Controller" })
@Slf4j
public class StateController {

	@Autowired
	private StateService stateService;

	@PostMapping("/api/addState")
	@ApiOperation(value = " Create a State")
	public ResponseEntity<State> addState(@RequestBody @Valid State state) {
		State createdState = stateService.save(state);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdState);
	}

	@GetMapping("/api/getStateById/{stateId}")
	@ApiOperation(value = " Get State  by Id")
	public ResponseEntity<State> getStateById(@PathVariable String stateId) {
		log.info("Request to get State  by Id:{}", stateId);
		return ResponseEntity.ok(stateService.getStateById(stateId));
	}

	@GetMapping("/api/getAllStates")
	@ApiOperation(value = " Get All State with deleted flag as false")
	public ResponseEntity<List<State>> GetAllState() {
		log.info("Request to get All States");
		return ResponseEntity.ok(stateService.getAllStates());
	}

	@DeleteMapping("/api/deleteStateById/{stateId}")
	@ApiOperation(value = " Delete State by Id")
	public ResponseEntity<State> deleteState(@PathVariable String stateId) {
		log.info("Request to delete State by Id:{}", stateId);
		return ResponseEntity.ok(stateService.deleteState(stateId));
	}

	@PutMapping("/api/updateStateById")
	@ApiOperation(value = "Update State  By Id")
	public ResponseEntity<State> updateStateById(@RequestBody State state) {
		log.info("Request to update State with id::{}", state.getId());
		return ResponseEntity.ok(stateService.updateState(state));
	}
}
