package com.home.essentials.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.home.essentials.model.Banners;
import com.home.essentials.service.BannersService;
import com.home.essentials.utils.AmazonClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Api(value = "Banners Controller", description = "REST API for Banner Images ", tags = { "Banners Controller" })
@Slf4j
public class BannersController {

	@Autowired
	private BannersService bannersService;
	
	@Autowired
	AmazonClient amazonClient;

	@PostMapping(path = "/api/admin/createBanners")
	@ApiOperation(value = "Upload Banner images to s3 and store the table in Db")
	public ResponseEntity<Banners> addBanner(
			@RequestParam(value = "bannerImage", required = false) List<MultipartFile> bannerImage,
			@RequestParam("bannerRequest") String bannerRequestString) throws GeneralSecurityException, IOException {
		Banners banners = null;
		try {
			banners = bannersService.save(bannerRequestString, bannerImage);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return new ResponseEntity<>(banners, HttpStatus.OK);
	}

	@PutMapping("/api/admin/updateBanner")
	@ApiOperation(value = "Update  Banner By Id")
	public ResponseEntity<Banners> updateBanners(
			@RequestParam(value = "file", required = false) List<MultipartFile> bannerImage,
			@RequestParam("bannerRequest") String bannerRequestString) throws Exception {

		Banners banners = null;
		try {
			banners = bannersService.updateBanners(bannerRequestString, bannerImage);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return new ResponseEntity<>(banners, HttpStatus.OK);
	}

	@GetMapping("/api/getBannerImageById/{bannerId}")
	@ApiOperation(value = " Get Banner images by Id")
	public ResponseEntity<Banners> getBannerImagesById(@PathVariable String bannerId) {
		log.info("Request to get banners  images  by Id:{}", bannerId);
		return ResponseEntity.ok(bannersService.getBannersById(bannerId));
	}

	@GetMapping("/api/getAllBannerImages")
	@ApiOperation(value = " Get All Banner Images with deleted flag as false")
	public ResponseEntity<List<Banners>> getAllBanners() {
		log.info("Request to get banners images");
		return ResponseEntity.ok(bannersService.getAllBanners());
	}

	@DeleteMapping("/api/deleteBannerImageById/{bannerId}")
	@ApiOperation(value = " Delete Banners images by Id")
	public ResponseEntity<Banners> deleteContactUsPage(@PathVariable String bannerId) {
		log.info("Request to delete contactUs Page by Id:{}", bannerId);
		return ResponseEntity.ok(bannersService.deleteBanners(bannerId));
	}
	
	@GetMapping("/api/getBannerImage")
	@ApiOperation(value = "Get Banner Image By image name")
	public ResponseEntity<?> getBannerImageByName(@RequestParam("bannerImage") String bannerImage)
			throws Exception {

		InputStream is = amazonClient.getFile(bannerImage);
		byte[] prodImage = IOUtils.toByteArray(is);

		return new ResponseEntity<>(prodImage, HttpStatus.OK);
	}

}
