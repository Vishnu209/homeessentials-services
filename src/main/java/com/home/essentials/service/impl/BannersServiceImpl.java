package com.home.essentials.service.impl;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.home.essentials.error.EntityNotFoundException;
import com.home.essentials.model.Banners;
import com.home.essentials.model.User;
import com.home.essentials.repository.BannersRepository;
import com.home.essentials.repository.UserRepository;
import com.home.essentials.service.BannersService;
import com.home.essentials.utils.AmazonClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BannersServiceImpl implements BannersService {

	@Autowired
	private BannersRepository bannersRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	AmazonClient amazonClient;

	@Override
	public Banners save(String bannersRequest, List<MultipartFile> bannerImageLt) throws Exception {

		Instant date = Instant.now();
		String formattedDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault())
				.format(date);

		Banners banner = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			banner = mapper.readValue(bannersRequest, Banners.class);
		} catch (IOException e) {
			log.error("Error while converting input string to product request!!", e);
			throw new Exception("Error while converting input string to product request!!");
		}

		List<String> bannerImageUrlLt = new ArrayList<String>();
		if (bannerImageLt != null && !bannerImageLt.isEmpty()) {
			log.info("banner image present!!!");

			for (MultipartFile bannerImage : bannerImageLt) {
				DBObject metadata = new BasicDBObject();
				metadata.put("materialType", "bannerImage");
				String contentS3Url;

				String filename = "Home Needs/Banners/" + formattedDate + "-"
						+ bannerImage.getOriginalFilename();
				// UPLOADING TO S3

				contentS3Url = amazonClient.uploadContent(bannerImage, null, metadata, filename,
						bannerImage.getInputStream());
				bannerImageUrlLt.add(contentS3Url);
			}
		}
		banner.setBannerS3Url(bannerImageUrlLt);
		banner.setCreatedDate(Instant.now());
		banner.setDeletedFlag(false);
		String user = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> loggedUser = userRepository.findByUsername(user);
		banner.setCreatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());

		banner = bannersRepository.save(banner);

		return banner;
	}

	@Override
	public Banners updateBanners(String bannersRequest, List<MultipartFile> bannerImageLt) throws Exception {
		Instant date = Instant.now();
		String formattedDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault())
				.format(date);

		Banners banner = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			banner = mapper.readValue(bannersRequest, Banners.class);
		} catch (IOException e) {
			log.error("Error while converting input string to product request!!", e);
			throw new Exception("Error while converting input string to product request!!");
		}
		Optional<Banners> bannerInDb = bannersRepository.findById(banner.getId());
		if (bannerInDb.isPresent()) {
			Banners editBanners = bannerInDb.get();

			// if banner image is not null, upload to s3 & set the url in bannerImageUrl,
			// else retain the previously saved image in bannerImageUrl

			List<String> bannerImageUrlLt = new ArrayList<String>();

			// ALWAYS DELETE EXISTING IMAGES FROM S3 AND SAVE THE NEW LIST
			List<String> existingBannerImageUrl = editBanners.getBannerS3Url() != null
					&& !editBanners.getBannerS3Url().isEmpty() ? editBanners.getBannerS3Url() : null;
			if (existingBannerImageUrl != null) {

				for (String imageUrl : existingBannerImageUrl) {

					amazonClient.deleteFileFromS3(imageUrl);
				}
			}
			if (bannerImageLt != null && !bannerImageLt.isEmpty()) {
				for (MultipartFile bannerImage : bannerImageLt) {
					DBObject metadata = new BasicDBObject();
					metadata.put("materialType", "bannerImage");
					String contentS3Url;
					String filename = "Home Needs/Banners/" + formattedDate + "-"
							+ bannerImage.getOriginalFilename();
					// UPLOADING TO S3
					contentS3Url = amazonClient.uploadContent(bannerImage, null, metadata, filename,
							bannerImage.getInputStream());
					bannerImageUrlLt.add(contentS3Url);

				}
				banner.setBannerS3Url(bannerImageUrlLt);
			}
			banner.setCreatedDate(editBanners.getCreatedDate());
			banner.setLastupdatedDate(Instant.now());
			banner.setDeletedFlag(false);
			String user = SecurityContextHolder.getContext().getAuthentication().getName();
			Optional<User> loggedUser = userRepository.findByUsername(user);
			banner.setUpdatedBy(loggedUser.get().getFirstName() + " " + loggedUser.get().getLastName());

			banner = bannersRepository.save(banner);
		} else {
			throw new Exception("banner not found in database");
		}
		return banner;
	}

	@Override
	public Banners getBannersById(String bannersId) {
		log.debug("Service request to get a banners by id");
		return bannersRepository.findById(bannersId)
				.orElseThrow(() -> new EntityNotFoundException(Banners.class, "bannersId", bannersId));
	}

	@Override
	public List<Banners> getAllBanners() {
		log.debug("Service request to get the list banner images");
		return bannersRepository.getAllBanners();
	}

	@Override
	public Banners deleteBanners(String bannersId) {
		log.debug("Service request to delete a banner image with id {}.", bannersId);
		Banners banners = bannersRepository.findById(bannersId)
				.orElseThrow(() -> new EntityNotFoundException(Banners.class, "bannersId", bannersId));
		banners.setDeletedFlag(true);
		return bannersRepository.save(banners);
	}

}
