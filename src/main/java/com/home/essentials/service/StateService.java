package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.State;

public interface StateService {

	State save(State state);

	State getStateById(String stateId);

	List<State> getAllStates();

	State updateState(State state);

	State deleteState(String stateId);

}
