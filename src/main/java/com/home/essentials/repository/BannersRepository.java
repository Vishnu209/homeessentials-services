package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.Banners;

public interface BannersRepository extends MongoRepository<Banners, String> {

	@Query(value = "{'deleted_flag' : false}")
	List<Banners> getAllBanners();

	@Query(value = "{'deleted_flag' : false, 'id':?0}")
	Optional<Banners> findById(String id);
}
