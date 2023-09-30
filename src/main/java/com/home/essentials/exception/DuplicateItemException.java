package com.home.essentials.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class DuplicateItemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateItemException(String id) {
		super(" Duplicate Item:   Item with id  " + id + "  already found ");
	}
}
