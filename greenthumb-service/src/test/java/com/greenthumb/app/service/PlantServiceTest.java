package com.greenthumb.app.service;

import com.greenthumb.app.client.TrefleClient;
import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.exception.ResourceNotFoundException;
import com.greenthumb.app.model.dto.trefle.TreflePlantDto;
import com.greenthumb.app.model.dto.trefle.TrefleSingleResponse;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.model.entity.Plant;
import com.greenthumb.app.model.entity.User;
import com.greenthumb.app.repository.PlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private GardenService gardenService;

    @Mock
    private TrefleClient trefleClient;

    @InjectMocks
    private PlantService plantService;

    private User user;
    private Garden garden;
    private Plant plant;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(plantService, "apiToken", "test-key");

        user = User.builder().username("testuser").build();
        garden = Garden.builder().id(1L).user(user).build();
        plant = Plant.builder().id(1L).nickname("Rose").trefleId(123).garden(garden).build();
    }



    @Test
    void addPlantToGarden_ShouldAddPlant_WhenValid() throws BusinessException {
        when(gardenService.getGardenById(1L, "testuser")).thenReturn(garden);
        

        TreflePlantDto plantData = new TreflePlantDto();
        plantData.setCommonName("Rose");
        plantData.setScientificName("Rosa");
        plantData.setImageUrl("test-url");

        TrefleSingleResponse response = new TrefleSingleResponse();
        response.setData(plantData);
        when(trefleClient.getPlantDetails(eq(123), any())).thenReturn(response);

        when(plantRepository.save(any(Plant.class))).thenReturn(plant);

        Plant created = plantService.addPlantToGarden(1L, 123, "My Rose", "custom-url", "testuser");

        assertNotNull(created);
        assertEquals(123, created.getTrefleId());
        verify(plantRepository).save(any(Plant.class));
    }



    @Test
    void addPlantToGarden_ShouldThrowException_WhenGardenNotFound() throws BusinessException {
        when(gardenService.getGardenById(1L, "testuser")).thenThrow(new ResourceNotFoundException("Garden not found"));

        assertThrows(ResourceNotFoundException.class, () -> plantService.addPlantToGarden(1L, 123, null, null, "testuser"));
    }



    @Test
    void addPlantToGarden_ShouldThrowException_WhenNotOwner() throws BusinessException {
        when(gardenService.getGardenById(1L, "otheruser")).thenThrow(new BusinessException("You are not the owner of this garden"));

        assertThrows(BusinessException.class, () -> plantService.addPlantToGarden(1L, 123, null, null, "otheruser"));
    }



    @Test
    void removePlantFromGarden_ShouldRemove_WhenOwner() throws BusinessException {
        when(plantRepository.findById(1L)).thenReturn(Optional.of(plant));
        
        plantService.removePlantFromGarden(1L, "testuser");

        verify(plantRepository).delete(plant);
    }
}
