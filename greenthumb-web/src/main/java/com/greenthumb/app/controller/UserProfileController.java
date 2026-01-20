package com.greenthumb.app.controller;

import com.greenthumb.app.model.dto.UserProfileDto;
import com.greenthumb.app.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    //Pobieranie danych profilowych
    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(@RequestAttribute("username") String username) {
        return ResponseEntity.ok(userProfileService.getProfile(username));
    }

    //Aktualizacja danych profilowych
    @PutMapping
    public ResponseEntity<UserProfileDto> updateProfile(@RequestAttribute("username") String username, @RequestBody UserProfileDto dto) {
        return ResponseEntity.ok(userProfileService.updateProfile(username, dto));
    }
}
