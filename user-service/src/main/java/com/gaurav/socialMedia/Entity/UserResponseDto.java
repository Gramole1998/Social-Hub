package com.gaurav.socialMedia.Entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserResponseDto {
    
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String bio;
    private String profileImageUrl;
    private Integer followersCount;
    private Integer followingCount;
    private Integer tweetsCount;
    private Boolean isVerified;
    private Boolean isActive;
    @Override
	public String toString() {
		return "UserResponseDto [id=" + id + ", username=" + username + ", email=" + email + ", fullName=" + fullName
				+ ", bio=" + bio + ", profileImageUrl=" + profileImageUrl + ", followersCount=" + followersCount
				+ ", followingCount=" + followingCount + ", tweetsCount=" + tweetsCount + ", isVerified=" + isVerified
				+ ", isActive=" + isActive + ", createdAt=" + createdAt + "]";
	}
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    // Constructors
    public UserResponseDto() {}
    
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.bio = user.getBio();
        this.profileImageUrl = user.getProfileImageUrl();
        this.followersCount = user.getFollowersCount();
        this.followingCount = user.getFollowingCount();
        this.tweetsCount = user.getTweetsCount();
        this.isVerified = user.getIsVerified();
        this.isActive = user.getIsActive();
        this.createdAt = user.getCreatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    
    public Integer getFollowersCount() { return followersCount; }
    public void setFollowersCount(Integer followersCount) { this.followersCount = followersCount; }
    
    public Integer getFollowingCount() { return followingCount; }
    public void setFollowingCount(Integer followingCount) { this.followingCount = followingCount; }
    
    public Integer getTweetsCount() { return tweetsCount; }
    public void setTweetsCount(Integer tweetsCount) { this.tweetsCount = tweetsCount; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

