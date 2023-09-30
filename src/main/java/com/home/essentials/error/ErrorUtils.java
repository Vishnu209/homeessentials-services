package com.home.essentials.error;

import java.util.function.Supplier;

import com.home.essentials.model.ContactUs;
import com.home.essentials.model.User;

public class ErrorUtils {

	public static Supplier<EntityNotFoundException> UserNotFoundException(String userId) {
		return () -> new EntityNotFoundException(User.class, "user", userId);
	}
	
	 public static Supplier<EntityNotFoundException> ContactUsNotFoundException(String contactUsId) {
	        return () -> new EntityNotFoundException(ContactUs.class, "contactUs", contactUsId);
	    }
}
