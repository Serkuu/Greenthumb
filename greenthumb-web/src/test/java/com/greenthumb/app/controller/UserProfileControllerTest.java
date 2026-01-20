package com.greenthumb.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenthumb.app.model.dto.UserProfileDto;
import com.greenthumb.app.service.UserProfileService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController userProfileController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
    }


    @Test
    void getProfile_ShouldReturnProfile() throws Exception {
        UserProfileDto dto = UserProfileDto.builder()
                .username("testuser")
                .bio("My Bio")
                .build();

        when(userProfileService.getProfile("testuser")).thenReturn(dto);

        mockMvc.perform(get("/api/profile")
                .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.bio").value("My Bio"));
    }


    @Test
    void updateProfile_ShouldReturnUpdatedProfile() throws Exception {
        UserProfileDto dto = UserProfileDto.builder()
                .bio("Updated Bio")
                .build();
        
        UserProfileDto responseDto = UserProfileDto.builder()
                .username("testuser")
                .bio("Updated Bio")
                .build();

        when(userProfileService.updateProfile(eq("testuser"), any(UserProfileDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.bio").value("Updated Bio"));
    }
}
