package com.gaurav.socialMedia.service;




import java.util.List;
import java.util.Optional;

import com.gaurav.socialMedia.Entity.UserRegistrationDto;
import com.gaurav.socialMedia.Entity.UserResponseDto;

public interface UserServiceInterface {
    
    UserResponseDto registerUser(UserRegistrationDto registrationDto);
    
    Optional<UserResponseDto> getUserById(Long id);
    
    Optional<UserResponseDto> getUserByUsername(String username);
    
    UserResponseDto updateUser(Long id, UserRegistrationDto updateDto);
    
    void deleteUser(Long id);
    
    List<UserResponseDto> searchUsers(String query);
    
    List<UserResponseDto> getAllActiveUsers();
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    void incrementFollowerCount(Long userId);
    
    void decrementFollowerCount(Long userId);
    
    void incrementFollowingCount(Long userId);
    
    void decrementFollowingCount(Long userId);
    
    void incrementTweetCount(Long userId);
}

