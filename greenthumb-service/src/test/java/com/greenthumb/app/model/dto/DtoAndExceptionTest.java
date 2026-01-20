package com.greenthumb.app.model.dto;

import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.exception.ResourceNotFoundException;
import com.greenthumb.app.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DtoAndExceptionTest {

    @Test
    void testExceptions() {
        BusinessException be = new BusinessException("Business error");
        assertEquals("Business error", be.getMessage());

        ResourceNotFoundException rnfe = new ResourceNotFoundException("Not found");
        assertEquals("Not found", rnfe.getMessage());

        UserAlreadyExistsException uaee = new UserAlreadyExistsException("User exists");
        assertEquals("User exists", uaee.getMessage());
    }

    @Test
    void testLoginRequest() {
        LoginRequest req = new LoginRequest();
        req.setUsername("user");
        req.setPassword("pass");

        assertEquals("user", req.getUsername());
        assertEquals("pass", req.getPassword());
        
        LoginRequest req2 = new LoginRequest("user", "pass");
        assertEquals(req.getUsername(), req2.getUsername());
    }

    @Test
    void testRegisterRequest() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("user");
        req.setPassword("pass");
        req.setEmail("email@example.com");

        assertEquals("user", req.getUsername());
        assertEquals("pass", req.getPassword());
        assertEquals("email@example.com", req.getEmail());

        RegisterRequest req2 = new RegisterRequest("user", "pass", "email@example.com");
        assertEquals(req.getUsername(), req2.getUsername());
    }

    @Test
    void testDtoBuildersAndAccessors() {
        UserProfileDto profile = UserProfileDto.builder()
                .username("user")
                .bio("bio")
                .build();
        assertEquals("user", profile.getUsername());
        assertEquals("bio", profile.getBio());

        AuthResponse auth = new AuthResponse("token", "http://url");
        assertEquals("token", auth.getToken());
        assertEquals("http://url", auth.getAvatarUrl());
        
        AddPlantRequest plant = new AddPlantRequest();
        plant.setNickname("Plant");
        assertEquals("Plant", plant.getNickname());
        
        CreateGardenRequest garden = new CreateGardenRequest();
        garden.setName("Garden");
        assertEquals("Garden", garden.getName());
    }
}
