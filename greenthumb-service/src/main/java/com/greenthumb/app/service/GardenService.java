package com.greenthumb.app.service;

import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.exception.ResourceNotFoundException;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.model.entity.User;
import com.greenthumb.app.repository.GardenRepository;
import com.greenthumb.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GardenService {

    private final GardenRepository gardenRepository;
    private final UserRepository userRepository;

    //Tworzenie nowego ogrodu
    @Transactional(rollbackFor = Exception.class)
    public Garden createGarden(String name, String username) throws BusinessException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Garden garden = Garden.builder()
                .name(name)
                .user(user)
                .build();

        return gardenRepository.save(garden);
    }

    //Pobieranie listy ogrodów
    @Transactional(readOnly = true)
    public List<Garden> getUserGardens(String username) throws BusinessException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return gardenRepository.findAllByUserId(user.getId());
    }

    //Usuwanie ogrodu
    @Transactional(rollbackFor = Exception.class)
    public void deleteGarden(Long gardenId, String username) throws BusinessException {
        Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Garden not found"));

        if (!garden.getUser().getUsername().equals(username)) {
            throw new BusinessException("You are not the owner of this garden");
        }

        gardenRepository.delete(garden);
    }
    
    //Pobieranie szczegółów ogrodu
    public Garden getGardenById(Long gardenId, String username) throws BusinessException {
         Garden garden = gardenRepository.findById(gardenId)
                .orElseThrow(() -> new ResourceNotFoundException("Garden not found"));
                
        if (!garden.getUser().getUsername().equals(username)) {
            throw new BusinessException("Nie jesteś właścicielem tego ogrodu"); // Tłumaczenie wyjątku
        }
        return garden;
    }
}
