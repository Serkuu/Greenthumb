package com.greenthumb.app.service;

import com.greenthumb.app.client.TrefleClient;
import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.exception.ResourceNotFoundException;
import com.greenthumb.app.model.dto.trefle.TrefleListResponse;
import com.greenthumb.app.model.dto.trefle.TreflePlantDto;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.model.entity.Plant;
import com.greenthumb.app.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final TrefleClient trefleClient;
    private final PlantRepository plantRepository;
    private final GardenService gardenService;

    @Value("${trefle.api.token}")
    private String apiToken;

    //Wyszukiwanie roślin w API
    public TrefleListResponse searchPlants(String query) {
        try {
            return trefleClient.searchPlants(query, apiToken);
        } catch (Exception e) {
            System.err.println("Trefle API search failed: " + e.getMessage());
            TrefleListResponse empty = new TrefleListResponse();
            empty.setData(java.util.Collections.emptyList());
            return empty;
        }
    }
    
    //Pobieranie szczegółów rośliny z API
    public TreflePlantDto getPlantDetails(int trefleId) {
       return trefleClient.getPlantDetails(trefleId, apiToken).getData();
    }

    //Dodawanie rośliny do ogrodu
    @Transactional(rollbackFor = Exception.class)
    public Plant addPlantToGarden(Long gardenId, Integer trefleId, String nickname, String imageUrl, String username) throws BusinessException {
        Garden garden = gardenService.getGardenById(gardenId, username);

        TreflePlantDto details = null;
        try {
            details = getPlantDetails(trefleId);
        } catch (Exception e) {
            System.err.println("Failed to fetch extended details for plant " + trefleId);
            e.printStackTrace();
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            if (details != null && details.getImageUrl() != null) {
                imageUrl = details.getImageUrl();
            }
        }

        if (nickname == null && details != null) {
            nickname = details.getCommonName();
        }

        Plant.PlantBuilder plantBuilder = Plant.builder()
                .trefleId(trefleId)
                .nickname(nickname != null ? nickname : "Plant " + trefleId)
                .imageUrl(imageUrl)
                .garden(garden);
                
        if (details != null) {
            plantBuilder.scientificName(details.getScientificName())
                        .family(details.getFamily())
                        .familyCommonName(details.getFamilyCommonName())
                        .year(details.getYear())
                        .edible(details.getEdible());
        }
        
        Plant plant = plantBuilder.build();

        return plantRepository.save(plant);
    }


    //Usuwanie rośliny z ogrodu
    @Transactional(rollbackFor = Exception.class)
    public void removePlantFromGarden(Long plantId, String username) throws BusinessException {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found"));

        if (!plant.getGarden().getUser().getUsername().equals(username)) {
            throw new BusinessException("You are not the owner of this plant");
        }

        plantRepository.delete(plant);
    }
}
