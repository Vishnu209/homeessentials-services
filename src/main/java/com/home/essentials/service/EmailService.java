package com.home.essentials.service;

import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.ContactUs;
import com.home.essentials.model.User;

public interface EmailService {

	@Async
	void sendUserCreationEmail(User user, String template, String emailSubject, List<String> adminUsers)
			throws Exception;
	
	@Async
	void sendUserCreationEmailToAdmin(User user, String template, String emailSubject, List<String> adminUsers)
			throws Exception;

	@Async
	void sendPasswordResetMail(User user);

	@Async
	void sendOrderCreationEmail(User user, HomeNeedsOrder order, String template, String emailSubject, List<String> adminUsers,
			Boolean isAdmin) throws Exception;
	
	@Async
	void sendOrderCreationEmailToAdmin(User user, HomeNeedsOrder order, String template, String emailSubject, List<String> adminUsers,
			Boolean isAdmin) throws Exception;

	
	@Async
	void sendOrderUpdationEmail(User user, HomeNeedsOrder order, String template, String emailSubject, List<String> adminUsers)
			throws Exception;

	@Async
	void sendOrderUpdationEmailToAdmin(User user, HomeNeedsOrder order, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception;

	@Async
	void sendContactUsCreationEmail(User user, ContactUs contactUs, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception;

	@Async
	void sendContactUsThanksEmailToUser(User user, ContactUs contactUs, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception;

}
