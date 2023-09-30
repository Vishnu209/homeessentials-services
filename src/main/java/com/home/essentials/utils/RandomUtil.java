package com.home.essentials.utils;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public final class RandomUtil {

	private static final int DEF_COUNT = 20;
	private static final int ID_COUNT = 6;

	private RandomUtil() {
	}

	/**
	 * Generate a password.
	 *
	 * @return the generated password
	 */
	public static String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
	}

	/**
	 * Generate an activation key.
	 *
	 * @return the generated activation key
	 */
	public static String generateActivationKey() {
		return RandomStringUtils.randomNumeric(DEF_COUNT);
	}
	
	/**
	 * Generate an meeting ID.
	 *
	 * @return the generated meeting ID
	 */
	public static String generateMeetingId() {
		return RandomStringUtils.randomNumeric(ID_COUNT);
	}
	
	
	/**
	 * Generate an meeting password.
	 *
	 * @return the generated meeting password
	 */
	public static String generateMeetingPassword() {
		return RandomStringUtils.randomNumeric(ID_COUNT);
	}



	/**
	 * Generate a reset key.
	 *
	 * @return the generated reset key
	 */
	public static String generateResetKey() {
		return RandomStringUtils.randomNumeric(DEF_COUNT);
	}

	public static String generateRandomPassword() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuffer salt = new StringBuffer();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}
	
	

}
