package com.home.essentials.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.FAQ;
import com.home.essentials.repository.FAQRepository;
import com.home.essentials.service.FAQService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FAQServiceImpl implements FAQService{

	@Autowired
	private FAQRepository faqRepository;
	
	@Override
	public FAQ save(FAQ faq) {
		log.debug("Service request to save a FAQ ");
		faq.setCreatedDate(Instant.now());
		faq = faqRepository.save(faq);
		return faq;
	}

	@Override
	public FAQ getFAQById(String faqId) {
		log.debug("Service request to get a FAQ  by id ");
		return faqRepository.getFAQById(faqId)
				.orElseThrow(() -> new EntityNotFoundException(FAQ.class, "faqId", faqId));
	}

	@Override
	public List<FAQ> getAllFAQs() {
		log.debug("Service request to get all FAQs");
		return faqRepository.getAllFAQs();
	}

	@Override
	public FAQ updateFAQ(FAQ faq) {
		log.debug("Service request to update a FAQ by id ");
		faq.setLastupdatedDate(Instant.now());
		return faqRepository.save(faq);
	}

	@Override
	public FAQ deleteFAQ(String faqId) {
		log.debug("Service request to delete a FAQ with id  {}.", faqId);
		FAQ faq = faqRepository.findById(faqId)
				.orElseThrow(() -> new EntityNotFoundException(FAQ.class, "faqId", faqId));
		faq.setDeletedFlag(true);
		return faqRepository.save(faq);
	}

}
