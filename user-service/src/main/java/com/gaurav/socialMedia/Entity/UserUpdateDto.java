package com.gaurav.socialMedia.Entity;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {
    
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;
    
    @Size(max = 160, message = "Bio cannot exceed 160 characters")
    private String bio;
    
    private String profileImageUrl;
    
    // Constructors
    public UserUpdateDto() {}
    
    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
