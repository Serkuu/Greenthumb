package com.greenthumb.app.service;

import com.greenthumb.app.model.dto.UserProfileDto;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.model.entity.Plant;
import com.greenthumb.app.model.entity.User;
import com.greenthumb.app.model.entity.UserProfile;
import com.greenthumb.app.repository.UserProfileRepository;
import com.greenthumb.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private User user;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userProfile = UserProfile.builder()
                .bio("I love plants")
                .avatarUrl("http://example.com/avatar.png")
                .build();

        user = User.builder()
                .username("testuser")
                .email("test@example.com")
                .userProfile(userProfile)
                .gardens(new ArrayList<>())
                .build();
    }


    @Test
    void getProfile_ShouldReturnProfile_WhenUserExists() {

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));


        UserProfileDto result = userProfileService.getProfile("testuser");


        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("I love plants", result.getBio());
        assertEquals(0, result.getGardenCount());
    }


    @Test
    void getProfile_ShouldCalculateStats_WhenGardensExist() {
        // Arrange
        Garden garden = new Garden();
        garden.setPlants(List.of(new Plant(), new Plant()));
        user.getGardens().add(garden);
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        UserProfileDto result = userProfileService.getProfile("testuser");

        // Assert
        assertEquals(1, result.getGardenCount());
        assertEquals(2, result.getPlantCount());
    }


    @Test
    void getProfile_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> userProfileService.getProfile("unknown"));
    }


    @Test
    void updateProfile_ShouldUpdateBioAndAvatar_WhenUserExists() {
        // Arrange
        UserProfileDto updateDto = UserProfileDto.builder()
                .bio("Updated bio")
                .avatarUrl("http://example.com/new.png")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserProfileDto result = userProfileService.updateProfile("testuser", updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated bio", result.getBio());
        assertEquals("http://example.com/new.png", result.getAvatarUrl());
        verify(userProfileRepository).save(any(UserProfile.class));
    }


    @Test
    void updateProfile_ShouldCreateProfile_WhenNoneExists() {
        // Arrange
        user.setUserProfile(null);
        UserProfileDto updateDto = UserProfileDto.builder().bio("New bio").build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> {
            UserProfile p = invocation.getArgument(0);
            user.setUserProfile(p);
            return p;
        });

        // Act
        UserProfileDto result = userProfileService.updateProfile("testuser", updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("New bio", result.getBio());
        assertNotNull(user.getUserProfile());
    }
}
