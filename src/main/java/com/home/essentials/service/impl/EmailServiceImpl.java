package com.home.essentials.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.model.ContactUs;
import com.home.essentials.model.OrderStatus;
import com.home.essentials.model.User;
import com.home.essentials.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	private static final String USER = "user";

	private static final String ORDER = "order";

	private static final String CONTACTUS = "contactUs";

	private static final String BASE_URL = "baseUrl";

	private static final String BASE_URL_HEROKU = "baseUrlHeroku";

	private final JavaMailSender mailSender;

	private final MessageSource messageSource;

	private final SpringTemplateEngine templateEngine;

	@Value("${mail.from}")
	private String from;

	@Value("${mail.bcc}")
	private String bcc;

	@Value("${mail.base-url}")
	private String base_url;

	@Value("${mail.base-url-heroku}")
	private String base_url_heroku;
	
	@Value("${company.name}")
	private String companyName;

	public EmailServiceImpl(JavaMailSender mailSender, MessageSource messageSource,
			SpringTemplateEngine templateEngine) {
		super();
		this.mailSender = mailSender;
		this.messageSource = messageSource;
		this.templateEngine = templateEngine;
	}

	@Override
	public void sendUserCreationEmail(User user, String template, String emailSubject, List<String> adminUsers)
			throws Exception {
		try {
			sendEmailFromTemplate(user, template, emailSubject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}

	}
	
	@Override
	public void sendUserCreationEmailToAdmin(User user, String template, String emailSubject, List<String> adminUsers)
			throws Exception {
		try {
			sendEmailFromTemplateToAdmin(user, template, emailSubject);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}
		
	}


	@Async
	public void sendEmailFromTemplate(User userInfo, String templateName, String titleKey) throws Exception {
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		context.setVariable(USER, userInfo);
		context.setVariable(BASE_URL, base_url);
		context.setVariable(BASE_URL_HEROKU, base_url_heroku);

		String content = templateEngine.process(templateName, context);
		// String subject = messageSource.getMessage(titleKey, null, locale);
		String subject = "Account Activation for " + companyName;

		try {
			sendEmailToUser(userInfo.getEmail(), subject, content, false, true, null, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + userInfo.getEmail());
		}
	}
	@Async
	public void sendEmailFromTemplateToAdmin(User userInfo, String templateName, String titleKey) throws Exception {
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		context.setVariable(USER, userInfo);
		context.setVariable(BASE_URL, base_url);
		context.setVariable(BASE_URL_HEROKU, base_url_heroku);

		String content = templateEngine.process(templateName, context);
		// String subject = messageSource.getMessage(titleKey, null, locale);
		String subject = "Account Activation for " + companyName;

		try {
			sendEmailToUser(userInfo.getEmail(), subject, content, false, true, null, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + userInfo.getEmail());
		}
	}

	private void sendEmailToUser(String to, String subject, String content, boolean isMultipart, boolean isHtml,
			List<String> adminEmails, Boolean isAdmin) throws Exception {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

			if (adminEmails != null) {
				String[] adminUserEmails = adminEmails.stream().toArray(String[]::new);
				if (isAdmin) {
					message.setTo(adminUserEmails);

				} else {
					message.setTo(to);
					// message.setCc(adminUserEmails);
				}
			} else {
				message.setTo(to);
			}
			message.addBcc(bcc, bcc);
			message.setFrom(from);
			message.setSubject(subject);
			message.setText(content, isHtml);

			mailSender.send(mimeMessage);
			// log.debug("Sent email to User '{}'", to);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.warn("Email could not be sent to user '{}'", to, e);
			} else {
				log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
			}
			e.printStackTrace();
			throw new Exception("Email was not sent to the user!!");
		}
	}

	@Override
	public void sendPasswordResetMail(User user) {
		log.debug("Sending password reset email to '{}'", user.getEmail());
		try {
			sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendOrderCreationEmail(User user, HomeNeedsOrder order, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception {

		try {
			sendOrderEmailFromTemplate(user, order, template, emailSubject, adminUsers, isAdmin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}
	}

	@Override
	public void sendOrderCreationEmailToAdmin(User user, HomeNeedsOrder order, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception {

		try {
			sendOrderEmailFromTemplateToAdmin(user, order, template, emailSubject, adminUsers, isAdmin);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}

	}

	@Async
	public void sendOrderEmailFromTemplate(User userInfo, HomeNeedsOrder orderInfo, String templateName, String titleKey,
			List<String> adminEmails, Boolean isAdmin) throws Exception {
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		context.setVariable(USER, userInfo);
		context.setVariable(ORDER, orderInfo);
		context.setVariable(BASE_URL, base_url);

		String content = templateEngine.process(templateName, context);
		// String subject = messageSource.getMessage(titleKey, null, locale);
		// Setting the subject of orders for users based on the Order Status
		String subject = null;

		if (orderInfo.getStatus().equals(OrderStatus.CREATED)) {
			subject = "Your " + companyName + " Order Confirmation " + orderInfo.getOrderNumber();
		}
		if (orderInfo.getStatus().equals(OrderStatus.DISPATCHED)) {
			subject = "Your " + companyName + "  Order " + orderInfo.getOrderNumber() + " Dispatched";
		}
		if (orderInfo.getStatus().equals(OrderStatus.DELIVERED)) {
			subject = "Your " + companyName + "  Order " + orderInfo.getOrderNumber() + " Delivered";
		}
		if (orderInfo.getStatus().equals(OrderStatus.CANCELLED)) {
			subject = "Your " + companyName + "  Order " + orderInfo.getOrderNumber() + " Cancelled";
		}
		if (orderInfo.getStatus().equals(OrderStatus.RETURNED)) {
			subject = "Your " + companyName + "  Order " + orderInfo.getOrderNumber() + " Return Confirmation";
		}
		if (orderInfo.getStatus().equals(OrderStatus.SUBMITTED)) {
			subject = "Your " + companyName + "  Order " + orderInfo.getOrderNumber() + " Submitted";
		}
		if (orderInfo.getStatus().equals(OrderStatus.OUT_FOR_DELIVERY)) {
			subject = "Your " + companyName + "  Order " + orderInfo.getOrderNumber() + " is Out For Delivery";

		}
		// subject = messageSource.getMessage(titleKey, null, locale);

		try {
			sendEmailToUser(userInfo.getEmail(), subject, content, false, true, adminEmails, isAdmin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + userInfo.getEmail());
		}
	}

	@Async
	public void sendOrderEmailFromTemplateToAdmin(User userInfo, HomeNeedsOrder orderInfo, String templateName, String titleKey,
			List<String> adminEmails, Boolean isAdmin) throws Exception {

		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		context.setVariable(USER, userInfo);
		context.setVariable(ORDER, orderInfo);
		context.setVariable(BASE_URL, base_url);

		String content = templateEngine.process(templateName, context);

		String subject = null;

		if (orderInfo.getStatus().equals(OrderStatus.CREATED)) {
			subject =  companyName + " Order Notification";
		}
		if (orderInfo.getStatus().equals(OrderStatus.CANCELLED)) {
			subject =  companyName +" Order " + orderInfo.getOrderNumber() + " Cancelled";
		}
		if (orderInfo.getStatus().equals(OrderStatus.FAILED)) {
			subject =  companyName + "  Order " + orderInfo.getOrderNumber() + " Failed";
		}

		try {
			sendEmailToUser(userInfo.getEmail(), subject, content, false, true, adminEmails, isAdmin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + userInfo.getEmail());
		}
	}

	@Override
	public void sendOrderUpdationEmail(User user, HomeNeedsOrder order, String template, String emailSubject,
			List<String> adminUsers) throws Exception {

		try {
			sendOrderEmailFromTemplate(user, order, template, emailSubject, adminUsers, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}

	}

	// if order is cancelled ,send email to admin
	@Override
	public void sendOrderUpdationEmailToAdmin(User user, HomeNeedsOrder order, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception {
		try {
			sendOrderEmailFromTemplateToAdmin(user, order, template, emailSubject, adminUsers, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}

	}

	// Sending ContactUs email to admin
	@Override
	public void sendContactUsCreationEmail(User user, ContactUs contactUs, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception {

		try {
			sendContactUsEmailFromTemplate(user, contactUs, template, emailSubject, adminUsers, isAdmin);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}

	}

	@Async
	public void sendContactUsEmailFromTemplate(User userInfo, ContactUs contactUsInfo, String templateName,
			String titleKey, List<String> adminEmails, Boolean isAdmin) throws Exception {
		Locale locale = Locale.forLanguageTag("en");
		Context context = new Context(locale);
		context.setVariable(USER, userInfo);
		context.setVariable(CONTACTUS, contactUsInfo);
		context.setVariable(BASE_URL, base_url);

		String content = templateEngine.process(templateName, context);
		String subject = messageSource.getMessage(titleKey, null, locale);

		try {
			sendEmailToUser(userInfo.getEmail(), subject, content, false, true, adminEmails, isAdmin);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("Email not sent::" + userInfo.getEmail());
		}
	}

	@Override
	public void sendContactUsThanksEmailToUser(User user, ContactUs contactUs, String template, String emailSubject,
			List<String> adminUsers, Boolean isAdmin) throws Exception {

		try {
			sendContactUsEmailFromTemplate(user, contactUs, template, emailSubject, adminUsers, isAdmin);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Email not sent::" + user.getEmail());
		}

	}

	
}
