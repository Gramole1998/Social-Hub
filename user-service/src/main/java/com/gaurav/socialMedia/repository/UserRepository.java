package com.gaurav.socialMedia.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gaurav.socialMedia.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  
	   Optional<User> findByUsername(@Param("username") String username);
	    
	    Optional<User> findByEmail(String email);
	    
	    boolean existsByUsername(String username);
	    
	    boolean existsByEmail(String email);
	    
	    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.createdAt DESC")
	    List<User> findActiveUsers();
	    
	    @Query("SELECT u FROM User u WHERE u.username LIKE %:query% OR u.fullName LIKE %:query%")
	    List<User> searchUsers(@Param("query") String query);
	    
	    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
	    Long countActiveUsers();

}
