package com.home.essentials.payload;

import lombok.Data;

@Data
public class FailureResponse {

	private String failure;

	public FailureResponse(String failure) {
		super();
		this.failure = failure;
	}
	
	
}
