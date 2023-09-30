package com.home.essentials.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;


@Data
@Document(collection = "PasswordResetToken")
public class PasswordResetToken {
	
		private static final long serialVersionUID = 6895708580308434381L;

		@Id
		private String id;

		@Field("reset_token")
		private String resetToken;

		@Field("user")
		private User user;
		
		@Field("password")
		private String password;

		@Field("confirmPassword")
		private String confirmPassword;
		
		@Field("expiry_date")
		private Instant expiryDate;

}
