package com.greenthumb.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private String bio;
    private String avatarUrl;

    private Integer gardenCount;
    private Integer plantCount;
}
