package com.home.essentials.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.home.essentials.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	
	 @Query(value = "{'email':?0 ,'deleted_flag':false}")
    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
    
    @Query(value = "{'id':?0 ,'deleted_flag':false}")
    Optional<User> findById(String id);

    @Query(value = "{'username':?0 ,'deleted_flag':false}")
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
	Optional<User> findOneByResetKey(String resetKey);
    
	@Query(value="{'is_Admin' : true, 'deleted_flag':false}")
    List<User> findTheUserByAdmin();

	@Query(value = "{'deleted_flag' : false}")
	List<User> getAllUsers(Sort sort);
}
