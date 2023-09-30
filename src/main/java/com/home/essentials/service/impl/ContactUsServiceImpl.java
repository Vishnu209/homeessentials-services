package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.ContactUs;
import com.home.essentials.model.ContactUsStatus;
import com.home.essentials.model.User;
import com.home.essentials.repository.ContactUsRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.ContactUsService;
import com.home.essentials.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContactUsServiceImpl implements ContactUsService {

	@Autowired
	private ContactUsRepository contactUsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public ContactUs save(ContactUs contactUs) {
		log.debug("Service request to save a ContactUs page");
		contactUs.setCreatedDate(Instant.now());
		contactUs.setContactUsStatus(ContactUsStatus.REQUESTED);
		contactUs = contactUsRepository.save(contactUs);

		try {
			List<User> adminUsers = userRepository.findTheUserByAdmin();
			List<String> adminEmails = new ArrayList<String>();
			for (User adminUser : adminUsers) {
				adminEmails.add(adminUser.getEmail());
			}
			//the mail will be send to the email provided in the contactUs request body
				User user = new User();
				user.setEmail(contactUs.getEmail());
				Optional<User> userWithName = userRepository.findByEmail(contactUs.getEmail());
				user.setFirstName(userWithName.get().getFirstName());
				// Contact us email for admin
				emailService.sendContactUsCreationEmail(user, contactUs, "mail/ContactUsCreationEmail",
						"email.contactus.title", adminEmails, true);

				// Email to the User
				emailService.sendContactUsCreationEmail(user, contactUs, "mail/ContactUsReplyEmailToUser.html",
						"email.contactus.reply.title", adminEmails, false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return contactUs;
	}

	@Override
	public ContactUs getContactUsById(String contactUsId) {
		log.debug("Service request to get a ContactUs page by id");
		return contactUsRepository.findById(contactUsId)
				.orElseThrow(() -> new EntityNotFoundException(ContactUs.class, "contactUsId", contactUsId));
	}

	@Override
	public List<ContactUs> getAllContactUs() {
		log.debug("Service request to get the list ContactUs pages");
		Sort sort = Sort.by(Sort.Direction.DESC, "createdDate", "lastupdatedDate");
		return contactUsRepository.getAllContactUsPages(sort);
	}

	@Override
	public ContactUs updateContactUs(ContactUs contactUs) {
		log.debug("Service request to update  a ContactUs page by id");
		contactUs.setLastUpdatedDate(Instant.now());
		
		try {
			List<User> adminUsers = userRepository.findTheUserByAdmin();
			List<String> adminEmails = new ArrayList<String>();
			for (User adminUser : adminUsers) {
				adminEmails.add(adminUser.getEmail());
			}
			//the mail will be send to the email provided in the contactUs request body
			User user = new User();
			user.setEmail(contactUs.getEmail());
			Optional<User> userWithName = userRepository.findByEmail(contactUs.getEmail());
			user.setFirstName(userWithName.get().getFirstName());
				// Email to the User after updating contact us page
				emailService.sendContactUsCreationEmail(user, contactUs, "mail/ContactUsReplyEmailToUserAfterUpdation",
						"email.contactus.reply.update.title", adminEmails, false);
				
				contactUs.setContactUsStatus(ContactUsStatus.REPLIED);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return contactUsRepository.save(contactUs);
	}

	@Override
	public ContactUs deleteContactUs(String contactUsId) {
		log.debug("Service request to delete a ContactUs page with id {}.", contactUsId);
		ContactUs contactUs = contactUsRepository.findById(contactUsId)
				.orElseThrow(() -> new EntityNotFoundException(ContactUs.class, "contactUsId", contactUsId));
		contactUs.setDeletedFlag(true);
		return contactUsRepository.save(contactUs);
	}

}
