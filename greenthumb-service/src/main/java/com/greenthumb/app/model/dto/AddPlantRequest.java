package com.greenthumb.app.model.dto;

import lombok.Data;

@Data
public class AddPlantRequest {
    private Long gardenId;
    private Integer trefleId;
    private String nickname;
    private String imageUrl;
}
