package com.greenthumb.app.controller;

import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.model.dto.AddPlantRequest;
import com.greenthumb.app.model.dto.trefle.TrefleListResponse;
import com.greenthumb.app.model.entity.Plant;
import com.greenthumb.app.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plants")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;

    //Wyszukiwanie roślin z Trefle API
    @GetMapping("/search")
    public ResponseEntity<TrefleListResponse> searchPlants(@RequestParam String q) {
        return ResponseEntity.ok(plantService.searchPlants(q));
    }

    //Dodawanie wybranej rośliny z Trefle API do ogrodu
    @PostMapping
    public ResponseEntity<Plant> addPlant(@RequestBody AddPlantRequest request, @RequestAttribute("username") String username) throws BusinessException {

        return ResponseEntity.ok(plantService.addPlantToGarden(
            request.getGardenId(),
            request.getTrefleId(),
            request.getNickname(),
            request.getImageUrl(),
            username
        ));
    }
    //Usuwanie rośliny z ogrodu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id, @RequestAttribute("username") String username) throws BusinessException {
        plantService.removePlantFromGarden(id, username);
        return ResponseEntity.noContent().build();
    }
}
