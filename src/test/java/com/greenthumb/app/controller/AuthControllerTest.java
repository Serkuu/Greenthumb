package com.greenthumb.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenthumb.app.config.JwtInterceptor;
import com.greenthumb.app.model.dto.AuthResponse;
import com.greenthumb.app.model.dto.LoginRequest;
import com.greenthumb.app.model.dto.RegisterRequest;
import com.greenthumb.app.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtInterceptor jwtInterceptor;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .build();
    }

    @Test
    void register_ShouldReturnOk_WhenRegistrationIsSuccessful() throws Exception {
        RegisterRequest request = new RegisterRequest("testuser", "test@test.com", "password");
        AuthResponse response = new AuthResponse("token", "avatarUrl");

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.avatarUrl").value("avatarUrl"));
    }

    @Test
    void login_ShouldReturnOk_WhenLoginIsSuccessful() throws Exception {
        LoginRequest request = new LoginRequest("testuser", "password");
        AuthResponse response = new AuthResponse("token", "avatarUrl");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.avatarUrl").value("avatarUrl"));
    }
}
