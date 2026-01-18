package com.greenthumb.app.service;

import com.greenthumb.app.exception.UserAlreadyExistsException;
import com.greenthumb.app.model.dto.AuthResponse;
import com.greenthumb.app.model.dto.LoginRequest;
import com.greenthumb.app.model.dto.RegisterRequest;
import com.greenthumb.app.model.entity.User;
import com.greenthumb.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    //Rejestracja nowego użytkownika w systemie
    public AuthResponse register(RegisterRequest request) {
        System.out.println("Register attempt for: " + request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(org.mindrot.jbcrypt.BCrypt.hashpw(request.getPassword(), org.mindrot.jbcrypt.BCrypt.gensalt()))
                .build();
        
        userRepository.save(user);

        //Generowanie tokenu JWT dla nowych userów
        var jwtToken = jwtService.generateToken(user.getUsername());
        
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    //Logowanie użytkownika do systemu
    public AuthResponse login(LoginRequest request) {
        System.out.println("Login attempt for user: " + request.getUsername());
        
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    System.out.println("User not found: " + request.getUsername());
                    return new RuntimeException("User not found");
                });

        if (!org.mindrot.jbcrypt.BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            System.out.println("Password mismatch for user: " + request.getUsername());
            throw new RuntimeException("Invalid credentials");
        }
                
        //Generowanie tokenu JWT dla sesji
        var jwtToken = jwtService.generateToken(user.getUsername());
        
        //Pobieranie URL awatara
        String avatarUrl = null;
        if (user.getUserProfile() != null) {
            avatarUrl = user.getUserProfile().getAvatarUrl();
        }

        return AuthResponse.builder()
                .token(jwtToken)
                .avatarUrl(avatarUrl)
                .build();
    }
}
