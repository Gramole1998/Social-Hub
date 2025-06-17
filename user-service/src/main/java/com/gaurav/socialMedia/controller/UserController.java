package com.gaurav.socialMedia.controller;


import com.gaurav.socialMedia.Entity.UserRegistrationDto;
import com.gaurav.socialMedia.Entity.UserResponseDto;
import com.gaurav.socialMedia.service.UserServiceInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "APIs for user registration, profile management, and user operations")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserServiceInterface userService;
    
    @Autowired
    public UserController(UserServiceInterface userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with the provided information")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            UserResponseDto user = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves user information by user ID")
    public ResponseEntity<?> getUserById(@Parameter(description = "User ID") @PathVariable Long id) {
        Optional<UserResponseDto> user = userService.getUserById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves user information by username")
    public ResponseEntity<?> getUserByUsername(@Parameter(description = "Username") @PathVariable String username) {
        Optional<UserResponseDto> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user profile", description = "Updates user profile information")
    public ResponseEntity<?> updateUser(@Parameter(description = "User ID") @PathVariable Long id, 
                                       @Valid @RequestBody UserRegistrationDto updateDto) {
        try {
            UserResponseDto updatedUser = userService.updateUser(id, updateDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Update failed: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Soft deletes a user account")
    public ResponseEntity<?> deleteUser(@Parameter(description = "User ID") @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Delete failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Searches for users by username or full name")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@Parameter(description = "Search query") @RequestParam String query) {
        List<UserResponseDto> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Get all active users", description = "Retrieves all active users")
    public ResponseEntity<List<UserResponseDto>> getAllActiveUsers() {
        List<UserResponseDto> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/check-username/{username}")
    @Operation(summary = "Check username availability", description = "Checks if a username is available")
    public ResponseEntity<Boolean> checkUsernameAvailability(@Parameter(description = "Username to check") @PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(!exists); // Return true if available (not exists)
    }
    
    @GetMapping("/check-email/{email}")
    @Operation(summary = "Check email availability", description = "Checks if an email is available")
    public ResponseEntity<Boolean> checkEmailAvailability(@Parameter(description = "Email to check") @PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(!exists); // Return true if available (not exists)
    }
    
    // Internal endpoints for other services
    @PostMapping("/{id}/increment-followers")
    @Operation(summary = "Increment follower count", description = "Internal API to increment user's follower count")
    public ResponseEntity<?> incrementFollowerCount(@PathVariable Long id) {
        try {
            userService.incrementFollowerCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/decrement-followers")
    @Operation(summary = "Decrement follower count", description = "Internal API to decrement user's follower count")
    public ResponseEntity<?> decrementFollowerCount(@PathVariable Long id) {
        try {
            userService.decrementFollowerCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/increment-following")
    @Operation(summary = "Increment following count", description = "Internal API to increment user's following count")
    public ResponseEntity<?> incrementFollowingCount(@PathVariable Long id) {
        try {
            userService.incrementFollowingCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/decrement-following")
    @Operation(summary = "Decrement following count", description = "Internal API to decrement user's following count")
    public ResponseEntity<?> decrementFollowingCount(@PathVariable Long id) {
        try {
            userService.decrementFollowingCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/{id}/increment-tweets")
    @Operation(summary = "Increment tweet count", description = "Internal API to increment user's tweet count")
    public ResponseEntity<?> incrementTweetCount(@PathVariable Long id) {
        try {
            userService.incrementTweetCount(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
