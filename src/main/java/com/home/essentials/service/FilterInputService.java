package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.FilterInput;

public interface FilterInputService {

	FilterInput save(FilterInput filterInput) throws Exception;

	FilterInput getFilterInputById(String filterInputId);

	List<FilterInput> getAllFilterInput();

	FilterInput updateFilterInput(FilterInput filterInput);

	FilterInput deleteFilterInput(String filterInputId);

}
