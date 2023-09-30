package com.home.essentials.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private String port;

	@Value("${mail.protocol}")
	private String protocol;

	@Value("${mail.username}")
	private String userName;

	@Value("${mail.password}")
	private String password;

	@Value("${mail.transport.protocol}")
	private String transportProtocol;

	@Value("${mail.smtps.auth}")
	private String auth;

	@Value("${mail.smtps.starttls.enable}")
	private String enable;

	@Value("${mail.smtps.timeout}")
	private String timeout;

	@Value("${mail.debug}")
	private String debug;

    @Value("${mail.from}")
    private String from;

	@Bean
	public JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

		javaMailSender.setHost(host);
		javaMailSender.setPort(Integer.parseInt(port));
		javaMailSender.setProtocol(protocol);
		javaMailSender.setUsername(userName);
		javaMailSender.setPassword(password);
		javaMailSender.setJavaMailProperties(getJavaMailProperties());

		return javaMailSender;
	}

	private Properties getJavaMailProperties() {
		Properties javaMailProps = new Properties();
		javaMailProps.setProperty("mail.transport.protocol", transportProtocol);
		javaMailProps.setProperty("mail.smtps.auth", auth);
		javaMailProps.setProperty("mail.smtps.starttls.enable", enable);
		javaMailProps.setProperty("mail.smtps.timeout", timeout);
		javaMailProps.setProperty("mail.debug", debug);
        javaMailProps.setProperty("mail.from", from);
		return javaMailProps;
	}

}
