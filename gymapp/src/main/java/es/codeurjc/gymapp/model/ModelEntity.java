package main.java.es.codeurjc.gymapp.model;
import java.lang.annotation.Inherited;

import jakarta.persistence.*;


@Entity
public abstract class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    public ModelEntity() {
    }

    public ModelEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
