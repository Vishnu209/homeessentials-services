package com.home.essentials.service;

import java.util.List;

import com.home.essentials.model.FAQ;

public interface FAQService {

	FAQ save(FAQ faq);

	FAQ getFAQById(String faqId);

	List<FAQ> getAllFAQs();

	FAQ updateFAQ(FAQ faq);

	FAQ deleteFAQ(String faqId);
}
