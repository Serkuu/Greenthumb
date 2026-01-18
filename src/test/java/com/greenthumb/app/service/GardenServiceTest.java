package com.greenthumb.app.service;

import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.exception.ResourceNotFoundException;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.model.entity.User;
import com.greenthumb.app.repository.GardenRepository;
import com.greenthumb.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GardenServiceTest {

    @Mock
    private GardenRepository gardenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GardenService gardenService;

    private User user;
    private Garden garden;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        garden = Garden.builder()
                .id(1L)
                .name("My Garden")
                .user(user)
                .build();
    }

    @Test
    void createGarden_ShouldReturnGarden_WhenUserExists() throws BusinessException {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(gardenRepository.save(any(Garden.class))).thenReturn(garden);

        Garden created = gardenService.createGarden("My Garden", "testuser");

        assertNotNull(created);
        assertEquals("My Garden", created.getName());
        verify(gardenRepository).save(any(Garden.class));
    }

    @Test
    void createGarden_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> gardenService.createGarden("My Garden", "testuser"));
    }

    @Test
    void getUserGardens_ShouldReturnList_WhenUserExists() throws BusinessException {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(gardenRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(garden));

        List<Garden> gardens = gardenService.getUserGardens("testuser");

        assertFalse(gardens.isEmpty());
        assertEquals(1, gardens.size());
    }

    @Test
    void deleteGarden_ShouldDelete_WhenUserIsOwner() throws BusinessException {
        when(gardenRepository.findById(1L)).thenReturn(Optional.of(garden));
        
        gardenService.deleteGarden(1L, "testuser");

        verify(gardenRepository).delete(garden);
    }

    @Test
    void deleteGarden_ShouldThrowException_WhenUserIsNotOwner() {
        when(gardenRepository.findById(1L)).thenReturn(Optional.of(garden));
        
        assertThrows(BusinessException.class, () -> gardenService.deleteGarden(1L, "otheruser"));
        verify(gardenRepository, never()).delete(any(Garden.class));
    }

    @Test
    void getGardenById_ShouldReturnGarden_WhenUserIsOwner() throws BusinessException {
         when(gardenRepository.findById(1L)).thenReturn(Optional.of(garden));

         Garden found = gardenService.getGardenById(1L, "testuser");

         assertNotNull(found);
         assertEquals(garden.getId(), found.getId());
    }
}
