package com.greenthumb.app.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;


    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1000 * 60 * 60); // 1 hour
    }


    @Test
    void extractUsername_ShouldReturnUsername_WhenTokenIsValid() {
        String token = jwtService.generateToken("testuser");
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }


    @Test
    void isTokenValid_ShouldReturnTrue_WhenUsernamesMatchAndTokenNotExpired() {
        String token = jwtService.generateToken("testuser");
        assertTrue(jwtService.isTokenValid(token, "testuser"));
    }


    @Test
    void isTokenValid_ShouldReturnFalse_WhenUsernamesDoNotMatch() {
        String token = jwtService.generateToken("testuser");
        assertFalse(jwtService.isTokenValid(token, "otheruser"));
    }


    @Test
    void isTokenValid_ShouldThrowException_WhenTokenIsExpired() {

        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000); // Expired immediately
        String token = jwtService.generateToken("testuser");
        
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, "testuser"));
    }
}
