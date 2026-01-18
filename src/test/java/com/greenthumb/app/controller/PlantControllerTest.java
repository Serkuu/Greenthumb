package com.greenthumb.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenthumb.app.model.dto.AddPlantRequest;
import com.greenthumb.app.model.dto.trefle.TrefleListResponse;
import com.greenthumb.app.model.entity.Plant;
import com.greenthumb.app.service.PlantService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PlantControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlantService plantService;

    @InjectMocks
    private PlantController plantController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(plantController).build();
    }

    //Powinien wyszukać rośliny
    @Test
    void searchPlants_ShouldReturnOk() throws Exception {
        when(plantService.searchPlants("rose")).thenReturn(new TrefleListResponse());

        mockMvc.perform(get("/api/plants/search")
                        .param("q", "rose"))
                .andExpect(status().isOk());
    }

    //Powinien dodać roślinę do ogrodu
    @Test
    void addPlant_ShouldReturnOk() throws Exception {
        AddPlantRequest request = new AddPlantRequest();
        request.setGardenId(1L);
        request.setTrefleId(123);
        request.setNickname("My Rose");

        when(plantService.addPlantToGarden(eq(1L), eq(123), eq("My Rose"), any(), any())).thenReturn(new Plant());

        mockMvc.perform(post("/api/plants")
                        .requestAttr("username", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    //Powinien usunąć roślinę z ogrodu
    @Test
    void deletePlant_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/plants/1")
                        .requestAttr("username", "testuser"))
                .andExpect(status().isNoContent());

        verify(plantService).removePlantFromGarden(1L, "testuser");
    }
}
