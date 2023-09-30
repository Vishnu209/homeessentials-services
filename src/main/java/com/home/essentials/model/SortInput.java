package com.home.essentials.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "SortInput")
public class SortInput {

	@Id
	private String id;

	@Field("sort_input")
	private String sortInput;

	@Field("code")
	private String code;   // example A-Z, L-H, H-L etc.
	
	@Field("sort_description")
	private String sortDesc;

	@Field("deleted_flag")
	private boolean deletedFlag;
}
