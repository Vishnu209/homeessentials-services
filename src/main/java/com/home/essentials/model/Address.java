package com.home.essentials.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
@Document(collection = "address")
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6895708580308434381L;

	@Id
	private String id;

	@Field("address_1")
	private String address_1;

	@Field("address_2")
	private String address_2;

	@Field("city")
	private String city;

	@Field("state")
	private String state;

	@Field("postcode")
	private String postcode;

	@Field("country")
	private String country;

}
