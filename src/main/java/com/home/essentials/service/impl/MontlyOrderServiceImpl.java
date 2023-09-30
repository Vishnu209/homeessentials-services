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
import com.home.essentials.model.HomeNeedsOrder;
import com.home.essentials.repository.OrderRepository;
import com.home.essentials.service.MonthlyOrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MontlyOrderServiceImpl implements MonthlyOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<MonthlyReports> getMonthlyOrders() {

		List<HomeNeedsOrder> ordersInDb = orderRepository.findAll();

		List<MonthlyReports> monthlyOrderReports = new ArrayList<MonthlyReports>();

		// creating new instances of each month
		MonthlyReports monthlyOrdersForJan = new MonthlyReports();
		MonthlyReports monthlyOrdersForFeb = new MonthlyReports();
		MonthlyReports monthlyOrdersForMar = new MonthlyReports();
		MonthlyReports monthlyOrdersForApr = new MonthlyReports();
		MonthlyReports monthlyOrdersForMay = new MonthlyReports();
		MonthlyReports monthlyOrdersForJun = new MonthlyReports();
		MonthlyReports monthlyOrdersForJul = new MonthlyReports();
		MonthlyReports monthlyOrdersForAug = new MonthlyReports();
		MonthlyReports monthlyOrdersForSep = new MonthlyReports();
		MonthlyReports monthlyOrdersForOct = new MonthlyReports();
		MonthlyReports monthlyOrdersForNov = new MonthlyReports();
		MonthlyReports monthlyOrdersForDec = new MonthlyReports();

		// creating a new list of orders for each month
		List<HomeNeedsOrder> ordersForJanuary = new ArrayList<>();
		List<HomeNeedsOrder> ordersForFebruary = new ArrayList<>();
		List<HomeNeedsOrder> ordersForMarch = new ArrayList<>();
		List<HomeNeedsOrder> ordersForApril = new ArrayList<>();
		List<HomeNeedsOrder> ordersForMay = new ArrayList<>();
		List<HomeNeedsOrder> ordersForJune = new ArrayList<>();
		List<HomeNeedsOrder> ordersForJuly = new ArrayList<>();
		List<HomeNeedsOrder> ordersForAugust = new ArrayList<>();
		List<HomeNeedsOrder> ordersForSeptember = new ArrayList<>();
		List<HomeNeedsOrder> ordersForOctober = new ArrayList<>();
		List<HomeNeedsOrder> ordersForNovember = new ArrayList<>();
		List<HomeNeedsOrder> ordersForDecember = new ArrayList<>();

		String formattedOrderDate = null;

		// Current year
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = localDate.getYear();
		String currentYear = String.valueOf(year);
		log.info("currrt yr = " + currentYear);

		// Iterating through each orders in the DB
		for (HomeNeedsOrder orders : ordersInDb) {
			if (orders.getCreatedDate() != null) {

				// converting each order's created date to the year-month format
				formattedOrderDate = DateTimeFormatter.ofPattern("yyyy-MM").withZone(ZoneId.systemDefault())
						.format(orders.getCreatedDate());

				// checking for each months

				if (formattedOrderDate.equals(currentYear + "-01")) {
					ordersForJanuary.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-02")) {
					ordersForFebruary.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-03")) {
					ordersForMarch.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-04")) {
					ordersForApril.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-05")) {
					ordersForMay.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-06")) {
					ordersForJune.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-07")) {
					ordersForJuly.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-08")) {
					ordersForAugust.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-09")) {
					ordersForSeptember.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-10")) {
					ordersForOctober.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-11")) {
					ordersForNovember.add(orders);
				}
				if (formattedOrderDate.equals(currentYear + "-12")) {
					ordersForDecember.add(orders);
				}
			}
		}
		log.info("count for jan = " + ordersForJanuary.size());
		log.info("count for feb = " + ordersForFebruary.size());
		log.info("count for mar = " + ordersForMarch.size());
		log.info("count for apr = " + ordersForApril.size());
		log.info("count for may = " + ordersForMay.size());
		log.info("count for jun = " + ordersForJune.size());
		log.info("count for jul = " + ordersForJuly.size());
		log.info("count for aug = " + ordersForAugust.size());
		log.info("count for sep = " + ordersForSeptember.size());
		log.info("count for oct = " + ordersForOctober.size());
		log.info("count for nov = " + ordersForNovember.size());
		log.info("count for dec = " + ordersForDecember.size());

		// setting the values to new instance of each month
		monthlyOrdersForJan.setMonth("Jan");
		monthlyOrdersForJan.setCount(ordersForJanuary.size());
		monthlyOrderReports.add(monthlyOrdersForJan);

		monthlyOrdersForFeb.setMonth("Feb");
		monthlyOrdersForFeb.setCount(ordersForFebruary.size());
		monthlyOrderReports.add(monthlyOrdersForFeb);

		monthlyOrdersForMar.setMonth("Mar");
		monthlyOrdersForMar.setCount(ordersForMarch.size());
		monthlyOrderReports.add(monthlyOrdersForMar);

		monthlyOrdersForApr.setMonth("Apr");
		monthlyOrdersForApr.setCount(ordersForApril.size());
		monthlyOrderReports.add(monthlyOrdersForApr);

		monthlyOrdersForMay.setMonth("May");
		monthlyOrdersForMay.setCount(ordersForMay.size());
		monthlyOrderReports.add(monthlyOrdersForMay);

		monthlyOrdersForJun.setMonth("Jun");
		monthlyOrdersForJun.setCount(ordersForJune.size());
		monthlyOrderReports.add(monthlyOrdersForJun);

		monthlyOrdersForJul.setMonth("Jul");
		monthlyOrdersForJul.setCount(ordersForJuly.size());
		monthlyOrderReports.add(monthlyOrdersForJul);

		monthlyOrdersForAug.setMonth("Aug");
		monthlyOrdersForAug.setCount(ordersForAugust.size());
		monthlyOrderReports.add(monthlyOrdersForAug);

		monthlyOrdersForSep.setMonth("Sep");
		monthlyOrdersForSep.setCount(ordersForSeptember.size());
		monthlyOrderReports.add(monthlyOrdersForSep);

		monthlyOrdersForOct.setMonth("Oct");
		monthlyOrdersForOct.setCount(ordersForOctober.size());
		monthlyOrderReports.add(monthlyOrdersForOct);

		monthlyOrdersForNov.setMonth("Nov");
		monthlyOrdersForNov.setCount(ordersForNovember.size());
		monthlyOrderReports.add(monthlyOrdersForNov);

		monthlyOrdersForDec.setMonth("Dec");
		monthlyOrdersForDec.setCount(ordersForDecember.size());
		monthlyOrderReports.add(monthlyOrdersForDec);

		return monthlyOrderReports;
	}
}