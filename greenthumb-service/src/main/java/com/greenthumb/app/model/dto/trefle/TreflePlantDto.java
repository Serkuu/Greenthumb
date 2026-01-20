package com.greenthumb.app.model.dto.trefle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TreflePlantDto {
    private int id;
    
    @JsonProperty("common_name")
    private String commonName;
    
    @JsonProperty("scientific_name")
    private String scientificName;
    
    @JsonProperty("image_url")
    private String imageUrl;

    private Object family;
    
    @JsonProperty("family_common_name")
    private String familyCommonName;
    
    private Integer year;
    
    private Boolean edible;

    @JsonProperty("main_species")
    private MainSpecies mainSpecies;
    
    @Data
    public static class MainSpecies {
        private String family;
        @JsonProperty("family_common_name")
        private String familyCommonName;
        private Integer year;
        private Boolean edible;
    }

    public String getFamily() {
        if (mainSpecies != null && mainSpecies.getFamily() != null) return mainSpecies.getFamily();
        
        if (family instanceof String) {
            return (String) family;
        } else if (family instanceof java.util.Map) {
            Object name = ((java.util.Map<?, ?>) family).get("name");
            return name != null ? name.toString() : null;
        }
        return null;
    }

    public String getFamilyCommonName() {
        if (mainSpecies != null && mainSpecies.getFamilyCommonName() != null) return mainSpecies.getFamilyCommonName();
        return familyCommonName;
    }
    public Integer getYear() {
         if (mainSpecies != null && mainSpecies.getYear() != null) return mainSpecies.getYear();
         return year;
    }
    public Boolean getEdible() {
         if (mainSpecies != null && mainSpecies.getEdible() != null) return mainSpecies.getEdible();
         return edible;
    }
}
