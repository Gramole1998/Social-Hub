package com.gaurav.socialMedia.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaurav.socialMedia.Entity.User;
import com.gaurav.socialMedia.Entity.UserRegistrationDto;
import com.gaurav.socialMedia.Entity.UserResponseDto;
import com.gaurav.socialMedia.exception.UserNotFoundException;
import com.gaurav.socialMedia.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
 
    private ObjectMapper objectMapper; 

    private static final String USER_CACHE_PREFIX = "cache:user:";
    private static final long CACHE_TTL = 1; // 1 hour

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RedisTemplate<String, Object> redisTemplate,
                           PasswordEncoder passwordEncoder,ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper=objectMapper;
    }

    @Override
    @Transactional // Only on methods that write data
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        log.info("Attempting to register user with username: {}", registrationDto.getUsername());
        
        if (existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        try {
            User user = new User();
            user.setUsername(registrationDto.getUsername());
            user.setEmail(registrationDto.getEmail());
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            user.setFullName(registrationDto.getFullName());
            user.setBio(registrationDto.getBio());

            User savedUser = userRepository.save(user);
            // Removed flush() - let Spring manage transactions
            
            log.info("User registered successfully with ID: {} and username: {}", 
                    savedUser.getId(), savedUser.getUsername());
            
            UserResponseDto response = new UserResponseDto(savedUser);
            cacheUser(savedUser); // Cache after successful save
            
            return response;
            
        } catch (Exception e) {
            log.error("Error registering user with username: {}", registrationDto.getUsername(), e);
            throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(Long id) {
        log.debug("Getting user by ID: {}", id);
        
        String cacheKey = USER_CACHE_PREFIX + id;
        Object cachedUser = redisTemplate.opsForValue().get(cacheKey);

        if (cachedUser != null) {
            log.debug("User found in cache for ID: {}", id);
            UserResponseDto dto = objectMapper.convertValue(cachedUser, UserResponseDto.class);
            return Optional.of(dto);
      }

        try {
            Optional<User> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                UserResponseDto userDto = new UserResponseDto(userOpt.get());
                redisTemplate.opsForValue().set(cacheKey, userDto, CACHE_TTL, TimeUnit.HOURS);
               log.debug("User found and cached for ID: {}", id);
                return Optional.of(userDto);
            }
            
            log.debug("User not found for ID: {}", id);
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error getting user by ID: {}", id, e);
            throw new RuntimeException("Failed to get user by ID: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByUsername(String username) {
        log.debug("Getting user by username: {}", username);
        
        try {
            Optional<User> user = userRepository.findByUsername(username);
            
            if (user.isPresent()) {
                log.debug("User found for username: {}", username);
                UserResponseDto response = new UserResponseDto(user.get());
                System.out.println(response);
                cacheUser(user.get());
                return Optional.of(response);
            } else {
                log.debug("User not found for username: {}", username);
                return Optional.empty();
            }
            
        } catch (Exception e) {
            log.error("Error getting user by username: {}", username, e);
            throw new RuntimeException("Failed to get user by username: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long id, UserRegistrationDto updateDto) {
        log.info("Updating user with ID: {}", id);
        
        User user = getUserOrThrow(id);

        if (updateDto.getFullName() != null) {
            user.setFullName(updateDto.getFullName());
        }
        if (updateDto.getBio() != null) {
            user.setBio(updateDto.getBio());
        }

        User updatedUser = userRepository.save(user);
        cacheUser(updatedUser);
        log.info("Updated user ID: {}", updatedUser.getId());

        return new UserResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deactivating user with ID: {}", id);
        
        User user = getUserOrThrow(id);
        user.setIsActive(false);
        userRepository.save(user);
        redisTemplate.delete(USER_CACHE_PREFIX + id);
        log.info("User deactivated: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsers(String query) {
        log.debug("Searching users with query: {}", query);
        
        try {
            return userRepository.searchUsers(query)
                    .stream()
                    .map(UserResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching users with query: {}", query, e);
            throw new RuntimeException("Failed to search users: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllActiveUsers() {
        log.debug("Getting all active users");
        
        try {
            return userRepository.findActiveUsers()
                    .stream()
                    .map(UserResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting all active users", e);
            throw new RuntimeException("Failed to get active users: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        try {
        	System.out.println(username);
            return userRepository.existsByUsername(username);
        } catch (Exception e) {
            log.error("Error checking if username exists: {}", username, e);
            throw new RuntimeException("Failed to check username existence: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            log.error("Error checking if email exists: {}", email, e);
            throw new RuntimeException("Failed to check email existence: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void incrementFollowerCount(Long userId) {
        User user = getUserOrThrow(userId);
        user.setFollowersCount(user.getFollowersCount() + 1);
        userRepository.save(user);
        cacheUser(user);
    }

    @Override
    @Transactional
    public void decrementFollowerCount(Long userId) {
        User user = getUserOrThrow(userId);
        user.setFollowersCount(Math.max(0, user.getFollowersCount() - 1));
        userRepository.save(user);
        cacheUser(user);
    }

    @Override
    @Transactional
    public void incrementFollowingCount(Long userId) {
        User user = getUserOrThrow(userId);
        user.setFollowingCount(user.getFollowingCount() + 1);
        userRepository.save(user);
        cacheUser(user);
    }

    @Override
    @Transactional
    public void decrementFollowingCount(Long userId) {
        User user = getUserOrThrow(userId);
        user.setFollowingCount(Math.max(0, user.getFollowingCount() - 1));
        userRepository.save(user);
        cacheUser(user);
    }

    @Override
    @Transactional
    public void incrementTweetCount(Long userId) {
        User user = getUserOrThrow(userId);
        user.setTweetsCount(user.getTweetsCount() + 1);
        userRepository.save(user);
        cacheUser(user);
    }

    @Transactional(readOnly = true)
    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private void cacheUser(User user) {
        try {
        	System.out.println("User: " + user);
        	System.out.println(USER_CACHE_PREFIX  + user.getId());

            String cacheKey = USER_CACHE_PREFIX + user.getId();
            System.out.println(cacheKey);
            UserResponseDto userDto = new UserResponseDto(user);
            
            redisTemplate.opsForValue().set(cacheKey, userDto, CACHE_TTL, TimeUnit.HOURS);
            log.debug("User cached with key: {}", cacheKey);
        } catch (Exception e) {
            log.warn("Failed to cache user with ID: {}", user.getId(), e);
            // Don't throw exception - caching failure shouldn't break the flow
        }
    }
}