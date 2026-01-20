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
    private Long id;

    @Column(nullable = false)
    private Integer trefleId;

    private String nickname;
    
    private String scientificName;
    private String family;
    private String familyCommonName;
    private Integer year;
    private Boolean edible;
    
    private String imageUrl;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garden_id", nullable = false)
    private Garden garden;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "plant_tags",
            joinColumns = @JoinColumn(name = "plant_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();
}
