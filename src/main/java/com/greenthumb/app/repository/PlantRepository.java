package com.greenthumb.app.repository;

import com.greenthumb.app.model.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findAllByGardenId(Long gardenId);
}
