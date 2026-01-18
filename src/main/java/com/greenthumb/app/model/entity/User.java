package com.greenthumb.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unikalny identyfikator użytkownika

    @Column(nullable = false, unique = true)
    private String username; // Nazwa użytkownika (unikalna)

    @Column(nullable = false)
    private String password; // Hasło (zahaszowane)

    @Column(nullable = false, unique = true)
    private String email; // Adres email (unikalny)

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile; // Profil użytkownika (relacja 1:1)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Garden> gardens; // Lista ogrodów użytkownika (relacja 1:N)
}
