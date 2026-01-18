package com.greenthumb.app.controller;

import com.greenthumb.app.exception.BusinessException;
import com.greenthumb.app.model.entity.Garden;
import com.greenthumb.app.service.GardenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gardens")
@RequiredArgsConstructor
public class GardenController {

    private final GardenService gardenService;

    //Tworzenie nowego ogrodu
    @PostMapping
    public ResponseEntity<Garden> createGarden(@RequestBody com.greenthumb.app.model.dto.CreateGardenRequest request, @RequestAttribute("username") String username) throws BusinessException {
        System.out.println("Received create garden request: " + request.getName() + " from user: " + username);
        return ResponseEntity.ok(gardenService.createGarden(request.getName(), username));
    }

    //Pobieranie listy ogrodów
    @GetMapping
    public ResponseEntity<List<Garden>> getUserGardens(@RequestAttribute("username") String username) throws BusinessException {
        return ResponseEntity.ok(gardenService.getUserGardens(username));
    }

    //Usuwanie ogrodu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarden(@PathVariable Long id, @RequestAttribute("username") String username) throws BusinessException {
        gardenService.deleteGarden(id, username);
        return ResponseEntity.noContent().build();
    }

    //Pobieranie szczegółów ogrodu
    @GetMapping("/{id}")
    public ResponseEntity<Garden> getGarden(@PathVariable Long id, @RequestAttribute("username") String username) throws BusinessException {
        return ResponseEntity.ok(gardenService.getGardenById(id, username));
    }
}
