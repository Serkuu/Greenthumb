package com.greenthumb.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenthumb.app.model.dto.CreateGardenRequest;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.service.GardenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GardenControllerTest {

    @Mock
    private GardenService gardenService;

    @InjectMocks
    private GardenController gardenController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gardenController).build();
    }


    @Test
    void createGarden_ShouldReturnCreatedGarden() throws Exception {
        CreateGardenRequest request = new CreateGardenRequest("My Garden");
        Garden garden = new Garden();
        garden.setId(1L);
        garden.setName("My Garden");

        when(gardenService.createGarden(eq("My Garden"), anyString())).thenReturn(garden);

        mockMvc.perform(post("/api/gardens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("My Garden"));
        
        verify(gardenService).createGarden("My Garden", "testuser");
    }


    @Test
    void getUserGardens_ShouldReturnListOfGardens() throws Exception {
        Garden garden = new Garden();
        garden.setName("Garden 1");
        
        when(gardenService.getUserGardens("testuser")).thenReturn(List.of(garden));

        mockMvc.perform(get("/api/gardens")
                .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Garden 1"));
    }


    @Test
    void deleteGarden_ShouldReturnNoContent() throws Exception {
        doNothing().when(gardenService).deleteGarden(1L, "testuser");

        mockMvc.perform(delete("/api/gardens/1")
                .requestAttr("username", "testuser"))
                .andExpect(status().isNoContent());

        verify(gardenService).deleteGarden(1L, "testuser");
    }


    @Test
    void getGarden_ShouldReturnGardenDetails() throws Exception {
        Garden garden = new Garden();
        garden.setId(1L);
        garden.setName("My Garden");

        when(gardenService.getGardenById(1L, "testuser")).thenReturn(garden);

        mockMvc.perform(get("/api/gardens/1")
                .requestAttr("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Garden"));
    }
}
