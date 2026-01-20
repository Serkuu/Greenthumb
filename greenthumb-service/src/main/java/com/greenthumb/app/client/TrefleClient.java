package com.greenthumb.app.client;

import com.greenthumb.app.model.dto.trefle.TrefleListResponse;

import com.greenthumb.app.model.dto.trefle.TrefleSingleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "trefleClient", url = "https://trefle.io/api/v1")
public interface TrefleClient {

    @GetMapping("/plants/search")
    TrefleListResponse searchPlants(@RequestParam("q") String query, @RequestParam("token") String token);

    @GetMapping("/plants/{id}")
    TrefleSingleResponse getPlantDetails(@PathVariable("id") int id, @RequestParam("token") String token);
}
