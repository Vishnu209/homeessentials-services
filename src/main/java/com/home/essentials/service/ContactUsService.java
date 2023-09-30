package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.ContactUs;

public interface ContactUsService {

	ContactUs save(ContactUs contactUs);
	
	ContactUs getContactUsById(String contactUsId);
	
	List<ContactUs> getAllContactUs();

	ContactUs updateContactUs(ContactUs contactUs);
	
	ContactUs deleteContactUs(String contactUsId);
}
