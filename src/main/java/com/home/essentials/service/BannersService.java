package com.home.essentials.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.home.essentials.model.Banners;

public interface BannersService {

	Banners save(String bannersRequest, List<MultipartFile> bannerImage)  throws Exception;

	Banners getBannersById(String bannersId);

	List<Banners> getAllBanners();

	Banners updateBanners(String bannersRequest, List<MultipartFile> bannerImage)  throws Exception;

	Banners deleteBanners(String bannersId);
}
