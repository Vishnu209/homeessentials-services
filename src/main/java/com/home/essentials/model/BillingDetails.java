package com.home.essentials.model;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

@Data
public class BillingDetails {

	@Field("first_name")
	private String firstName;

	@Field("last_name")
	private String lastName;

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

	@Field("email")
	private String email;

	@Field("mobile_no")
	private String mobileNo;

}
