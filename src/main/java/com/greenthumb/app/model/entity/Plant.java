package com.greenthumb.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "plants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unikalny identyfikator rośliny w bazie

    @Column(nullable = false)
    private Integer trefleId; // ID rośliny w zewnętrznym API (Trefle)

    private String nickname; // Nazwa własna nadana przez użytkownika
    
    private String scientificName; // Nazwa naukowa (łacińska)
    private String family; // Rodzina roślin
    private String familyCommonName; // Pospolita nazwa rodziny
    private Integer year; // Rok odkrycia/opisania
    private Boolean edible; // Czy jadalna
    
    private String imageUrl; // URL do zdjęcia rośliny

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    private Garden garden; // Ogród, do którego należy roślina

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "plant_tags",
            joinColumns = @JoinColumn(name = "plant_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
    
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPlants().add(this);
    }
    
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPlants().remove(this);
    }
}
