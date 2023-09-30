package com.home.essentials.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "roles")
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -310968250419043358L;

	@Id
	private String id;

	@Field("name")
	private String name;

}
