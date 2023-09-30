package com.home.essentials.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2490210576879638239L;

	@Id
	private String id;

	@Field("first_name")
	private String firstName;

	@Field("last_name")
	private String lastName;

	@Field("username")
	private String username;

	@Field("email")
	private String email;

	@Field("mobile_no")
	private String mobileno;

	@Field("password")
	private String password;
	
	@Field("admin_password")
	private String adminPassword; 
	

	@Field("social_login")
	private boolean SocialLogin;

	@Field("social_fb_login")
	private boolean SocialFbLogin;

	@Field("deleted_flag")
	private boolean deletedFlag = false;

	private String accessToken;

	@Field("created_by")
	private String createdBy;

	@Field("updated_by")
	private String updatedBy;

	@Field("is_admin")
	private boolean isAdmin;

	@Field("enabled")
	private boolean enabled;

	@Field("wishlist")
	private List<Wishlist> wishlist;

	@Field("confirmation_token")
	private String confirmationToken;

	@Field("shopping_cart")
	private List<ShoppingCartEntry> shoppingCartEntries;

	@Field("billing_address")
	private Address billingAddress;

	@Field("shipping_address")
	private List<Address> shippingAddress;

	@Field("created_date")
	private Instant createdDate = Instant.now();

	@Field("updated_date")
	private Instant lastupdatedDate;

	private Set<Role> roles = new HashSet<>();
	
	@Size(max = 20)
	@Field("reset_key")
	@JsonIgnore
	private String resetKey;

	@Field("reset_date")
	private Instant resetDate = null;

}