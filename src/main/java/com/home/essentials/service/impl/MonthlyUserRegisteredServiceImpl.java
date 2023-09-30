package com.home.essentials.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.dto.MonthlyReports;
import com.home.essentials.model.User;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.MonthlyUsersRegisteredService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MonthlyUserRegisteredServiceImpl implements MonthlyUsersRegisteredService {

	@Autowired
	private UserRepository userRepository;

	// Number of users registered in each month
	@Override
	public List<MonthlyReports> getMonthlyUsers() {

		List<MonthlyReports> monthlyUserReport = new ArrayList<MonthlyReports>();

		List<User> usersinDb = userRepository.findAll();

		// creating new instances of each month
		MonthlyReports monthlyUsersForJan = new MonthlyReports();
		MonthlyReports monthlyUsersForFeb = new MonthlyReports();
		MonthlyReports monthlyUsersForMar = new MonthlyReports();
		MonthlyReports monthlyUsersForApr = new MonthlyReports();
		MonthlyReports monthlyUsersForMay = new MonthlyReports();
		MonthlyReports monthlyUsersForJun = new MonthlyReports();
		MonthlyReports monthlyUsersForJul = new MonthlyReports();
		MonthlyReports monthlyUsersForAug = new MonthlyReports();
		MonthlyReports monthlyUsersForSep = new MonthlyReports();
		MonthlyReports monthlyUsersForOct = new MonthlyReports();
		MonthlyReports monthlyUsersForNov = new MonthlyReports();
		MonthlyReports monthlyUsersForDec = new MonthlyReports();

		List<User> usersForJanuary = new ArrayList<>();
		List<User> usersForFebruary = new ArrayList<>();
		List<User> usersForMarch = new ArrayList<>();
		List<User> usersForApril = new ArrayList<>();
		List<User> usersForMay = new ArrayList<>();
		List<User> usersForJune = new ArrayList<>();
		List<User> usersForJuly = new ArrayList<>();
		List<User> usersForAugust = new ArrayList<>();
		List<User> usersForSeptember = new ArrayList<>();
		List<User> usersForOctober = new ArrayList<>();
		List<User> usersForNovember = new ArrayList<>();
		List<User> usersForDecember = new ArrayList<>();

		String formattedOrderDate = null;

		// Current year
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = localDate.getYear();
		String currentYear = String.valueOf(year);
		log.info("currrt yr = " + currentYear);

		// Iterating through each orders in the DB
		for (User users : usersinDb) {
			if (users.getCreatedDate() != null) {

				// converting each user's created date to the year-month format
				formattedOrderDate = DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault())
						.format(users.getCreatedDate());

				// checking for each months

				if (formattedOrderDate.equals(currentYear + "-01")) {
					usersForJanuary.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-02")) {
					usersForFebruary.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-03")) {
					usersForMarch.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-04")) {
					usersForApril.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-05")) {
					usersForMay.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-06")) {
					usersForJune.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-07")) {
					usersForJuly.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-08")) {
					usersForAugust.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-09")) {
					usersForSeptember.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-10")) {
					usersForOctober.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-11")) {
					usersForNovember.add(users);
				}
				if (formattedOrderDate.equals(currentYear + "-12")) {
					usersForDecember.add(users);
				}
			}
		}
		log.info("count for jan = " + usersForJanuary.size());
		log.info("count for feb = " + usersForFebruary.size());
		log.info("count for mar = " + usersForMarch.size());
		log.info("count for apr = " + usersForApril.size());
		log.info("count for may = " + usersForMay.size());
		log.info("count for jun = " + usersForJune.size());
		log.info("count for jul = " + usersForJuly.size());
		log.info("count for aug = " + usersForAugust.size());
		log.info("count for sep = " + usersForSeptember.size());
		log.info("count for oct = " + usersForOctober.size());
		log.info("count for nov = " + usersForNovember.size());
		log.info("count for dec = " + usersForDecember.size());

		// setting the values to new instance of each month
		monthlyUsersForJan.setMonth("Jan");
		monthlyUsersForJan.setCount(usersForJanuary.size());
		monthlyUserReport.add(monthlyUsersForJan);

		monthlyUsersForFeb.setMonth("Feb");
		monthlyUsersForFeb.setCount(usersForFebruary.size());
		monthlyUserReport.add(monthlyUsersForFeb);

		monthlyUsersForMar.setMonth("Mar");
		monthlyUsersForMar.setCount(usersForMarch.size());
		monthlyUserReport.add(monthlyUsersForMar);

		monthlyUsersForApr.setMonth("Apr");
		monthlyUsersForApr.setCount(usersForApril.size());
		monthlyUserReport.add(monthlyUsersForApr);

		monthlyUsersForMay.setMonth("May");
		monthlyUsersForMay.setCount(usersForMay.size());
		monthlyUserReport.add(monthlyUsersForMay);

		monthlyUsersForJun.setMonth("Jun");
		monthlyUsersForJun.setCount(usersForJune.size());
		monthlyUserReport.add(monthlyUsersForJun);

		monthlyUsersForJul.setMonth("Jul");
		monthlyUsersForJul.setCount(usersForJuly.size());
		monthlyUserReport.add(monthlyUsersForJul);

		monthlyUsersForAug.setMonth("Aug");
		monthlyUsersForAug.setCount(usersForAugust.size());
		monthlyUserReport.add(monthlyUsersForAug);

		monthlyUsersForSep.setMonth("Sep");
		monthlyUsersForSep.setCount(usersForSeptember.size());
		monthlyUserReport.add(monthlyUsersForSep);

		monthlyUsersForOct.setMonth("Oct");
		monthlyUsersForOct.setCount(usersForOctober.size());
		monthlyUserReport.add(monthlyUsersForOct);

		monthlyUsersForNov.setMonth("Nov");
		monthlyUsersForNov.setCount(usersForNovember.size());
		monthlyUserReport.add(monthlyUsersForNov);

		monthlyUsersForDec.setMonth("Dec");
		monthlyUsersForDec.setCount(usersForDecember.size());
		monthlyUserReport.add(monthlyUsersForDec);

		return monthlyUserReport;
	}
}
