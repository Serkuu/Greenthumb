package com.greenthumb.app.service;

import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.model.dto.UserProfileDto;
import com.greenthumb.app.model.entity.User;
import com.greenthumb.app.model.entity.UserProfile;
import com.greenthumb.app.repository.UserProfileRepository;
import com.greenthumb.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    //Pobieranie profilu użytkownika
    @Transactional(readOnly = true)
    public UserProfileDto getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = user.getUserProfile();
        
        //Obliczanie statystyk
        int gardenCount = user.getGardens() != null ? user.getGardens().size() : 0;
        int plantCount = user.getGardens() != null ? user.getGardens().stream()
                .mapToInt(g -> g.getPlants() != null ? g.getPlants().size() : 0)
                .sum() : 0;
        
        return UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(profile != null ? profile.getBio() : "")
                .avatarUrl(profile != null ? profile.getAvatarUrl() : null)
                .gardenCount(gardenCount)
                .plantCount(plantCount)
                .build();
    }

    //Aktualizacja profilu użytkownika po edytowaniu przez usera
    @Transactional
    public UserProfileDto updateProfile(String username, UserProfileDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = user.getUserProfile();
        if (profile == null) {
            profile = UserProfile.builder().user(user).build();
            user.setUserProfile(profile);
        }

        if (dto.getBio() != null) profile.setBio(dto.getBio());
        if (dto.getAvatarUrl() != null) profile.setAvatarUrl(dto.getAvatarUrl());

        userProfileRepository.save(profile);

        return getProfile(username);
    }
}
