package com.home.essentials.model;

import java.util.Date;

import lombok.Data;

@Data
public class MonthlyOrderReport {

	private Date startDate ;
	
	private Date endDate;
	
	private String sessionsWithEvent;
	
	private String eventValue;
}
