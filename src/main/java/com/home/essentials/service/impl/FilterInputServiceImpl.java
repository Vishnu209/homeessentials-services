package com.home.essentials.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.FilterInput;
import com.home.essentials.repository.FilterInputRepository;
import com.home.essentials.repository.SortInputRepository;
import com.home.essentials.service.FilterInputService;

@Service
public class FilterInputServiceImpl implements FilterInputService {

	@Autowired
	private FilterInputRepository filterInputRepository;
	
	@Autowired
	private SortInputRepository sortInputRepo; 

	@Override
	public FilterInput save(FilterInput filterInput) throws Exception {
		if(!filterInputRepository.findAll().isEmpty()) {
			throw new Exception("filter input table already present ");
		}
		filterInput = filterInputRepository.save(filterInput);
		return filterInput;
	}

	@Override
	public FilterInput getFilterInputById(String filterInputId) {
		return filterInputRepository.findById(filterInputId)
				.orElseThrow(() -> new EntityNotFoundException(FilterInput.class, "filterInputId", filterInputId));
	}

	@Override
	public List<FilterInput> getAllFilterInput() {
		List<FilterInput> filterInLt = filterInputRepository.getAllFilterInput();
		filterInLt.forEach(filterIn->{
			if(filterIn.getSortInputs()==null ||filterIn.getSortInputs().isEmpty()) {
				filterIn.setSortInputs(sortInputRepo.getAllFilterInput());
				filterInputRepository.save(filterIn);
			}
		
		});
		return filterInLt;
	}

	@Override
	public FilterInput updateFilterInput(FilterInput filterInput) {
		return filterInputRepository.save(filterInput);
	}

	@Override
	public FilterInput deleteFilterInput(String filterInputId) {
		FilterInput filterInput = filterInputRepository.findById(filterInputId)
				.orElseThrow(() -> new EntityNotFoundException(FilterInput.class, "filterInputId", filterInputId));
		filterInput.setDeletedFlag(true);
		return filterInputRepository.save(filterInput);
	}

}
