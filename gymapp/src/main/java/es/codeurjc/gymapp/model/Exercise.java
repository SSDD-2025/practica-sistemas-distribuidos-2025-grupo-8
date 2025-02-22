package es.codeurjc.gymapp.model;

import jakarta.persistence.*;

@Entity
public class Exercise{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    private Material material;
    @ManyToOne
    private Routine routine;

    public Exercise() {
    }

    public Exercise(String name,String description, Material material) {
        this.name = name;
        this.description = description;
        this.material = material;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine){
        this.routine = routine;
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
