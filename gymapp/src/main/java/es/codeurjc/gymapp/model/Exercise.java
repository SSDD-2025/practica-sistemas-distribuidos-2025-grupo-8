package es.codeurjc.gymapp.model;

import jakarta.persistence.*;

@Entity
public class Exercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
