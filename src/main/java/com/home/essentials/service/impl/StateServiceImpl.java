package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.State;
import com.home.essentials.repository.StateRepository;
import com.home.essentials.service.StateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StateServiceImpl implements StateService {

	@Autowired
	private StateRepository stateRepository;

	@Override
	public State save(State state) {
		log.debug("Service request to save a State ");
		state.setCreatedDate(Instant.now());
		state = stateRepository.save(state);
		return state;
	}

	@Override
	public State getStateById(String stateId) {
		log.debug("Service request to get a State by id ");
		return stateRepository.findById(stateId)
				.orElseThrow(() -> new EntityNotFoundException(State.class, "stateId", stateId));
	}

	@Override
	public List<State> getAllStates() {
		log.debug("Service request to get all States");
		return stateRepository.getAllStates();
	}

	@Override
	public State updateState(State state) {
		log.debug("Service request to update a State by id ");
		state.setLastupdatedDate(Instant.now());
		return stateRepository.save(state);
	}

	@Override
	public State deleteState(String stateId) {
		log.debug("Service request to delete a State with id  {}.", stateId);
		State state = stateRepository.findById(stateId)
				.orElseThrow(() -> new EntityNotFoundException(State.class, "stateId", stateId));
		state.setDeletedFlag(true);
		return stateRepository.save(state);

	}

}
